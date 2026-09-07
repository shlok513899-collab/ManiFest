package com.example.service

import android.content.Context
import android.content.Intent
import com.example.audio.FrequencyAudioEngine
import com.example.model.FrequencyPreset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class SleepTimerDuration(val minutes: Int, val label: String) {
    OFF(0, "Off"),
    M15(15, "15 min"),
    M30(30, "30 min"),
    M45(45, "45 min"),
    M60(60, "60 min")
}

object PlaybackStateManager {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var sleepTimerJob: Job? = null

    val audioEngine = FrequencyAudioEngine()

    private val _currentPreset = MutableStateFlow<FrequencyPreset?>(null)
    val currentPreset: StateFlow<FrequencyPreset?> = _currentPreset.asStateFlow()

    private val _playlist = MutableStateFlow<List<FrequencyPreset>>(emptyList())
    val playlist: StateFlow<List<FrequencyPreset>> = _playlist.asStateFlow()

    val isPlaying: StateFlow<Boolean> = audioEngine.isPlayingState
    val amplitude: StateFlow<Float> = audioEngine.currentWaveAmplitude

    private val _masterVolume = MutableStateFlow(0.85f)
    val masterVolume: StateFlow<Float> = _masterVolume.asStateFlow()

    private val _toneVolume = MutableStateFlow(0.75f)
    val toneVolume: StateFlow<Float> = _toneVolume.asStateFlow()

    private val _noiseVolume = MutableStateFlow(0.5f)
    val noiseVolume: StateFlow<Float> = _noiseVolume.asStateFlow()

    private val _isLooping = MutableStateFlow(true)
    val isLooping: StateFlow<Boolean> = _isLooping.asStateFlow()

    private val _sleepTimer = MutableStateFlow(SleepTimerDuration.OFF)
    val sleepTimer: StateFlow<SleepTimerDuration> = _sleepTimer.asStateFlow()

    private val _sleepSecondsRemaining = MutableStateFlow<Int?>(null)
    val sleepSecondsRemaining: StateFlow<Int?> = _sleepSecondsRemaining.asStateFlow()

    fun playPreset(context: Context, preset: FrequencyPreset, currentPlaylist: List<FrequencyPreset> = emptyList()) {
        _currentPreset.value = preset
        if (currentPlaylist.isNotEmpty()) {
            _playlist.value = currentPlaylist
        }

        audioEngine.setConfig(preset.toneConfig)
        audioEngine.play()

        // Start Foreground Service
        AudioPlaybackService.start(context)
    }

    fun togglePlayPause(context: Context) {
        if (isPlaying.value) {
            pause()
            AudioPlaybackService.pause(context)
        } else {
            if (_currentPreset.value != null) {
                audioEngine.play()
                AudioPlaybackService.start(context)
            }
        }
    }

    fun pause() {
        audioEngine.pause()
    }

    fun resume(context: Context) {
        if (_currentPreset.value != null) {
            audioEngine.play()
            AudioPlaybackService.start(context)
        }
    }

    fun stop(context: Context) {
        audioEngine.stop()
        cancelSleepTimer()
        AudioPlaybackService.stop(context)
    }

    fun playNext(context: Context) {
        val list = _playlist.value
        val current = _currentPreset.value ?: return
        if (list.isEmpty()) return
        val idx = list.indexOfFirst { it.id == current.id }
        if (idx != -1 && idx + 1 < list.size) {
            playPreset(context, list[idx + 1])
        } else if (_isLooping.value && list.isNotEmpty()) {
            playPreset(context, list[0])
        }
    }

    fun playPrevious(context: Context) {
        val list = _playlist.value
        val current = _currentPreset.value ?: return
        if (list.isEmpty()) return
        val idx = list.indexOfFirst { it.id == current.id }
        if (idx > 0) {
            playPreset(context, list[idx - 1])
        } else if (_isLooping.value && list.isNotEmpty()) {
            playPreset(context, list[list.size - 1])
        }
    }

    fun setMasterVolume(vol: Float) {
        _masterVolume.value = vol
        audioEngine.setMasterVolume(vol)
    }

    fun setToneVolume(vol: Float) {
        _toneVolume.value = vol
        audioEngine.setToneVolume(vol)
    }

    fun setNoiseVolume(vol: Float) {
        _noiseVolume.value = vol
        audioEngine.setNoiseVolume(vol)
    }

    fun toggleLoop() {
        _isLooping.value = !_isLooping.value
    }

    fun setSleepTimer(duration: SleepTimerDuration, context: Context? = null) {
        _sleepTimer.value = duration
        sleepTimerJob?.cancel()

        if (duration == SleepTimerDuration.OFF) {
            _sleepSecondsRemaining.value = null
            audioEngine.setSleepFadeFactor(1.0f)
            return
        }

        val totalSeconds = duration.minutes * 60
        _sleepSecondsRemaining.value = totalSeconds
        audioEngine.setSleepFadeFactor(1.0f)

        sleepTimerJob = scope.launch {
            var remaining = totalSeconds
            while (remaining > 0) {
                delay(1000)
                remaining--
                _sleepSecondsRemaining.value = remaining

                // In final 30 seconds, smoothly fade out volume
                if (remaining <= 30) {
                    val fadeFactor = (remaining.toFloat() / 30f).coerceIn(0f, 1f)
                    audioEngine.setSleepFadeFactor(fadeFactor)
                }
            }

            // Timer expired: stop playback smoothly
            audioEngine.setSleepFadeFactor(0f)
            audioEngine.pause()
            _sleepTimer.value = SleepTimerDuration.OFF
            _sleepSecondsRemaining.value = null
            audioEngine.setSleepFadeFactor(1.0f)

            if (context != null) {
                AudioPlaybackService.pause(context)
            }
        }
    }

    fun cancelSleepTimer() {
        sleepTimerJob?.cancel()
        _sleepTimer.value = SleepTimerDuration.OFF
        _sleepSecondsRemaining.value = null
        audioEngine.setSleepFadeFactor(1.0f)
    }
}
