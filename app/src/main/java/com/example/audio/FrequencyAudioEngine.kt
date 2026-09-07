package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import com.example.model.NoiseColor
import com.example.model.SynthesisMode
import com.example.model.ToneConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

class FrequencyAudioEngine {

    companion object {
        const val SAMPLE_RATE = 44100
        private const val TWO_PI = 2.0 * PI
        private const val BUFFER_FRAMES = 2048
        private const val BUFFER_SIZE_FLOATS = BUFFER_FRAMES * 2 // Stereo = 2 channels
    }

    private var audioTrack: AudioTrack? = null
    private var synthesisThread: Thread? = null
    private val isPlaying = AtomicBoolean(false)
    private val stateLock = Any()

    // Current synthesis configuration
    @Volatile
    private var currentConfig: ToneConfig = ToneConfig(
        mode = SynthesisMode.PURE_TONE,
        pureToneHz = 432f
    )

    // Volumes
    @Volatile private var masterVolume: Float = 0.85f
    @Volatile private var toneVolume: Float = 0.75f
    @Volatile private var noiseVolume: Float = 0.5f

    // Sleep timer ramp factor (1.0 down to 0.0)
    @Volatile private var sleepFadeFactor: Float = 1.0f

    // Phase accumulators for continuous waves
    private var phaseLeft = 0.0
    private var phaseRight = 0.0
    private var phaseCarrier = 0.0
    private var phasePulse = 0.0

    // Noise filter states
    private var lastBrownLeft = 0.0f
    private var lastBrownRight = 0.0f
    private var lastPinkLeft = 0.0f
    private var lastPinkRight = 0.0f

    // Click-prevention envelope
    private var rampGain = 0.0f
    private var targetRampGain = 1.0f
    private val rampStep = 1.0f / (SAMPLE_RATE * 0.04f) // 40ms smooth ramp

    // Amplitude state flow for UI visualizer
    private val _currentWaveAmplitude = MutableStateFlow(0f)
    val currentWaveAmplitude: StateFlow<Float> = _currentWaveAmplitude.asStateFlow()

    private val _isPlayingState = MutableStateFlow(false)
    val isPlayingState: StateFlow<Boolean> = _isPlayingState.asStateFlow()

    init {
        initAudioTrack()
    }

    private fun initAudioTrack() {
        val minBufferSize = AudioTrack.getMinBufferSize(
            SAMPLE_RATE,
            AudioFormat.CHANNEL_OUT_STEREO,
            AudioFormat.ENCODING_PCM_FLOAT
        )
        val bufferSize = maxOf(minBufferSize, BUFFER_SIZE_FLOATS * 4)

        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()

        val format = AudioFormat.Builder()
            .setSampleRate(SAMPLE_RATE)
            .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
            .setEncoding(AudioFormat.ENCODING_PCM_FLOAT)
            .build()

        audioTrack = AudioTrack.Builder()
            .setAudioAttributes(attributes)
            .setAudioFormat(format)
            .setBufferSizeInBytes(bufferSize)
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build()
    }

    fun setConfig(config: ToneConfig) {
        currentConfig = config
    }

    fun setMasterVolume(volume: Float) {
        masterVolume = volume.coerceIn(0f, 1f)
    }

    fun setToneVolume(volume: Float) {
        toneVolume = volume.coerceIn(0f, 1f)
    }

    fun setNoiseVolume(volume: Float) {
        noiseVolume = volume.coerceIn(0f, 1f)
    }

    fun setSleepFadeFactor(factor: Float) {
        sleepFadeFactor = factor.coerceIn(0f, 1f)
    }

    fun play() {
        synchronized(stateLock) {
            if (isPlaying.get()) return

            // Stop any previous thread if still alive
            isPlaying.set(false)
            try {
                audioTrack?.pause()
                audioTrack?.flush()
            } catch (ignored: Exception) {}
            try {
                synthesisThread?.join(150)
            } catch (ignored: Exception) {}
            synthesisThread = null

            if (audioTrack == null || audioTrack?.state != AudioTrack.STATE_INITIALIZED) {
                initAudioTrack()
            }

            try {
                audioTrack?.play()
            } catch (e: Exception) {
                initAudioTrack()
                try {
                    audioTrack?.play()
                } catch (ignored: Exception) {}
            }

            rampGain = 0.0f
            targetRampGain = 1.0f
            isPlaying.set(true)
            _isPlayingState.value = true

            synthesisThread = Thread({ synthesisLoop() }, "FrequencySynthesisThread").apply {
                priority = Thread.MAX_PRIORITY
                start()
            }
        }
    }

    fun pause() {
        synchronized(stateLock) {
            if (!isPlaying.get()) {
                _isPlayingState.value = false
                _currentWaveAmplitude.value = 0f
                return
            }

            // Immediately mark as paused so UI responds instantly
            isPlaying.set(false)
            _isPlayingState.value = false
            _currentWaveAmplitude.value = 0f

            // Immediately pause and flush AudioTrack to stop sound and unblock writes
            try {
                audioTrack?.pause()
                audioTrack?.flush()
            } catch (ignored: Exception) {}

            // Wait cleanly for synthesis thread to exit
            try {
                synthesisThread?.join(150)
            } catch (ignored: Exception) {}
            synthesisThread = null
        }
    }

    fun stop() {
        synchronized(stateLock) {
            isPlaying.set(false)
            _isPlayingState.value = false
            _currentWaveAmplitude.value = 0f
            targetRampGain = 0.0f
            rampGain = 0.0f

            try {
                audioTrack?.pause()
                audioTrack?.flush()
                audioTrack?.stop()
            } catch (ignored: Exception) {}

            try {
                synthesisThread?.join(150)
            } catch (ignored: Exception) {}
            synthesisThread = null
        }
    }

    fun release() {
        synchronized(stateLock) {
            stop()
            try {
                audioTrack?.release()
            } catch (ignored: Exception) {}
            audioTrack = null
        }
    }

    private fun synthesisLoop() {
        val buffer = FloatArray(BUFFER_SIZE_FLOATS)
        var frameCounter = 0

        while (isPlaying.get()) {
            val config = currentConfig
            val effectiveMasterVol = masterVolume * sleepFadeFactor
            val effectiveToneVol = toneVolume * effectiveMasterVol
            val effectiveNoiseVol = noiseVolume * effectiveMasterVol

            var rmsSum = 0f

            for (i in 0 until BUFFER_FRAMES) {
                // Smooth click-prevention ramp
                if (rampGain < targetRampGain) {
                    rampGain = (rampGain + rampStep).coerceAtMost(targetRampGain)
                } else if (rampGain > targetRampGain) {
                    rampGain = (rampGain - rampStep).coerceAtLeast(targetRampGain)
                }

                var leftSample = 0f
                var rightSample = 0f

                when (config.mode) {
                    SynthesisMode.PURE_TONE -> {
                        val freq = config.pureToneHz.toDouble()
                        val sample = sin(phaseLeft).toFloat() * effectiveToneVol
                        leftSample = sample
                        rightSample = sample

                        phaseLeft = (phaseLeft + (TWO_PI * freq / SAMPLE_RATE)) % TWO_PI
                    }

                    SynthesisMode.BINAURAL_BEAT -> {
                        // True stereo: left receives baseHz, right receives baseHz + beatHz
                        val leftFreq = config.baseHz.toDouble()
                        val rightFreq = (config.baseHz + config.beatOrPulseHz).toDouble()

                        leftSample = sin(phaseLeft).toFloat() * effectiveToneVol
                        rightSample = sin(phaseRight).toFloat() * effectiveToneVol

                        phaseLeft = (phaseLeft + (TWO_PI * leftFreq / SAMPLE_RATE)) % TWO_PI
                        phaseRight = (phaseRight + (TWO_PI * rightFreq / SAMPLE_RATE)) % TWO_PI
                    }

                    SynthesisMode.ISOCHRONIC_PULSE -> {
                        // Single carrier gated by pulse rate LFO
                        val carrierFreq = config.carrierHz.toDouble()
                        val pulseFreq = config.beatOrPulseHz.toDouble()

                        // Gating envelope: smooth raised cosine between 0 and 1
                        val gate = 0.5f * (1.0f + sin(phasePulse).toFloat())
                        val carrier = sin(phaseCarrier).toFloat() * effectiveToneVol
                        val sample = carrier * gate

                        leftSample = sample
                        rightSample = sample

                        phaseCarrier = (phaseCarrier + (TWO_PI * carrierFreq / SAMPLE_RATE)) % TWO_PI
                        phasePulse = (phasePulse + (TWO_PI * pulseFreq / SAMPLE_RATE)) % TWO_PI
                    }

                    SynthesisMode.COLORED_NOISE -> {
                        val (nL, nR) = generateNoisePair(config.noiseColor)
                        leftSample = nL * effectiveNoiseVol
                        rightSample = nR * effectiveNoiseVol
                    }

                    SynthesisMode.NOISE_PLUS_BINAURAL -> {
                        // Mix noise layer + binaural beat layer
                        val leftFreq = config.baseHz.toDouble()
                        val rightFreq = (config.baseHz + config.beatOrPulseHz).toDouble()

                        val tL = sin(phaseLeft).toFloat() * effectiveToneVol
                        val tR = sin(phaseRight).toFloat() * effectiveToneVol

                        phaseLeft = (phaseLeft + (TWO_PI * leftFreq / SAMPLE_RATE)) % TWO_PI
                        phaseRight = (phaseRight + (TWO_PI * rightFreq / SAMPLE_RATE)) % TWO_PI

                        val (nL, nR) = generateNoisePair(config.noiseColor)

                        leftSample = tL + (nL * effectiveNoiseVol)
                        rightSample = tR + (nR * effectiveNoiseVol)
                    }

                    SynthesisMode.NOISE_PLUS_PURE -> {
                        val freq = config.pureToneHz.toDouble()
                        val tSample = sin(phaseLeft).toFloat() * effectiveToneVol
                        phaseLeft = (phaseLeft + (TWO_PI * freq / SAMPLE_RATE)) % TWO_PI

                        val (nL, nR) = generateNoisePair(config.noiseColor)

                        leftSample = tSample + (nL * effectiveNoiseVol)
                        rightSample = tSample + (nR * effectiveNoiseVol)
                    }
                }

                // Apply envelope and clamp
                val outLeft = (leftSample * rampGain).coerceIn(-1.0f, 1.0f)
                val outRight = (rightSample * rampGain).coerceIn(-1.0f, 1.0f)

                buffer[i * 2] = outLeft
                buffer[i * 2 + 1] = outRight

                rmsSum += (outLeft * outLeft + outRight * outRight) * 0.5f
            }

            if (!isPlaying.get()) break

            // Write interleaved float buffer to AudioTrack
            val track = audioTrack ?: break
            val written = try {
                track.write(buffer, 0, BUFFER_SIZE_FLOATS, AudioTrack.WRITE_BLOCKING)
            } catch (e: Exception) {
                -1
            }

            if (written < 0 || !isPlaying.get()) {
                break
            }

            // Update amplitude for visualizer periodically (every ~2000 frames is ~45ms)
            frameCounter++
            if (frameCounter % 2 == 0) {
                val rms = kotlin.math.sqrt(rmsSum / BUFFER_FRAMES).coerceIn(0f, 1f)
                _currentWaveAmplitude.value = rms
            }
        }

        // Post-loop cleanup
        _currentWaveAmplitude.value = 0f
    }

    private fun generateNoisePair(color: NoiseColor): Pair<Float, Float> {
        val whiteL = (Random.nextFloat() * 2f) - 1f
        val whiteR = (Random.nextFloat() * 2f) - 1f

        return when (color) {
            NoiseColor.WHITE -> Pair(whiteL * 0.6f, whiteR * 0.6f)

            NoiseColor.PINK -> {
                // -3dB/octave gentle filter
                lastPinkLeft = 0.98f * lastPinkLeft + 0.02f * whiteL
                lastPinkRight = 0.98f * lastPinkRight + 0.02f * whiteR
                Pair(lastPinkLeft * 3.2f, lastPinkRight * 3.2f)
            }

            NoiseColor.BROWN -> {
                // Leaky integrator: out[n] = (out[n-1] + 0.02 * white[n]) / 1.02 * 3.5
                lastBrownLeft = (lastBrownLeft + 0.02f * whiteL) / 1.02f
                lastBrownRight = (lastBrownRight + 0.02f * whiteR) / 1.02f
                Pair(
                    (lastBrownLeft * 3.5f).coerceIn(-1f, 1f),
                    (lastBrownRight * 3.5f).coerceIn(-1f, 1f)
                )
            }
        }
    }
}
