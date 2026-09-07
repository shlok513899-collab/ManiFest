package com.example.model

data class FrequencyPreset(
    val category: String,
    val title: String,
    val audio_type: String,
    val technical_hz: String,
    val target_outcome: String,
    val tags: List<String> = emptyList()
) {
    val id: String get() = "${category}_${title}".replace(" ", "_")

    val toneConfig: ToneConfig by lazy {
        ToneConfig.parse(audio_type, technical_hz)
    }
}

enum class SynthesisMode {
    PURE_TONE,
    BINAURAL_BEAT,
    ISOCHRONIC_PULSE,
    COLORED_NOISE,
    NOISE_PLUS_BINAURAL,
    NOISE_PLUS_PURE
}

enum class NoiseColor {
    WHITE,
    PINK,
    BROWN
}

data class ToneConfig(
    val mode: SynthesisMode,
    val baseHz: Float = 200f,
    val beatOrPulseHz: Float = 10f,
    val pureToneHz: Float = 432f,
    val carrierHz: Float = 200f,
    val noiseColor: NoiseColor = NoiseColor.BROWN,
    val hasNoise: Boolean = false,
    val hasTone: Boolean = true,
    val isStereoHeadphonesRequired: Boolean = false
) {
    companion object {
        fun parse(audioType: String, technicalHz: String): ToneConfig {
            val typeClean = audioType.trim()
            val hzClean = technicalHz.trim()

            // 1. Combo type: Noise + Binaural
            if (typeClean.contains("Noise", ignoreCase = true) && typeClean.contains("Binaural", ignoreCase = true)) {
                val noiseColor = parseNoiseColor(hzClean)
                // Extract beat Hz if present, e.g. "Brown Noise + 10 Hz Alpha"
                val beatMatch = Regex("""([\d.]+)\s*Hz""").find(hzClean)
                val beatHz = beatMatch?.groupValues?.get(1)?.toFloatOrNull() ?: 8f
                return ToneConfig(
                    mode = SynthesisMode.NOISE_PLUS_BINAURAL,
                    baseHz = 130f,
                    beatOrPulseHz = beatHz,
                    noiseColor = noiseColor,
                    hasNoise = true,
                    hasTone = true,
                    isStereoHeadphonesRequired = true
                )
            }

            // 2. Combo type: Noise + Pure
            if (typeClean.contains("Noise", ignoreCase = true) && (typeClean.contains("Pure", ignoreCase = true) || typeClean.contains("+"))) {
                val noiseColor = parseNoiseColor(hzClean)
                val hzMatch = Regex("""([\d.]+)\s*Hz""").find(hzClean)
                val pureHz = hzMatch?.groupValues?.get(1)?.toFloatOrNull() ?: 432f
                return ToneConfig(
                    mode = SynthesisMode.NOISE_PLUS_PURE,
                    pureToneHz = pureHz,
                    noiseColor = noiseColor,
                    hasNoise = true,
                    hasTone = true,
                    isStereoHeadphonesRequired = false
                )
            }

            // 3. Colored Noise
            if (typeClean.equals("Colored Noise", ignoreCase = true) || typeClean.contains("Noise", ignoreCase = true)) {
                return ToneConfig(
                    mode = SynthesisMode.COLORED_NOISE,
                    noiseColor = parseNoiseColor(hzClean),
                    hasNoise = true,
                    hasTone = false,
                    isStereoHeadphonesRequired = false
                )
            }

            // 4. Binaural Beat: "BASE Hz base / BEAT Hz ___"
            if (typeClean.equals("Binaural Beat", ignoreCase = true) || typeClean.contains("Binaural", ignoreCase = true)) {
                val numbers = Regex("""[\d.]+""").findAll(hzClean).mapNotNull { it.value.toFloatOrNull() }.toList()
                val base = if (numbers.isNotEmpty()) numbers[0] else 140f
                val beat = if (numbers.size >= 2) numbers[1] else 8f
                return ToneConfig(
                    mode = SynthesisMode.BINAURAL_BEAT,
                    baseHz = base,
                    beatOrPulseHz = beat,
                    hasNoise = false,
                    hasTone = true,
                    isStereoHeadphonesRequired = true
                )
            }

            // 5. Isochronic Pulse
            if (typeClean.equals("Isochronic Pulse", ignoreCase = true) || typeClean.contains("Isochronic", ignoreCase = true)) {
                val numbers = Regex("""[\d.]+""").findAll(hzClean).mapNotNull { it.value.toFloatOrNull() }.toList()
                val pulseHz = if (numbers.isNotEmpty()) numbers[0] else 10f
                val carrier = if (numbers.size >= 2) numbers[1] else 200f
                return ToneConfig(
                    mode = SynthesisMode.ISOCHRONIC_PULSE,
                    carrierHz = carrier,
                    beatOrPulseHz = pulseHz,
                    hasNoise = false,
                    hasTone = true,
                    isStereoHeadphonesRequired = false
                )
            }

            // 6. Pure tone: Solfeggio, Chakra, Planetary, Angel Frequency, Harmonic Tone, Harmonic Resonance, Resonant Tone
            val numbers = Regex("""[\d.]+""").findAll(hzClean).mapNotNull { it.value.toFloatOrNull() }.toList()
            val freq = if (numbers.isNotEmpty()) numbers[0] else 432f
            return ToneConfig(
                mode = SynthesisMode.PURE_TONE,
                pureToneHz = freq,
                hasNoise = false,
                hasTone = true,
                isStereoHeadphonesRequired = false
            )
        }

        private fun parseNoiseColor(hzString: String): NoiseColor {
            val lower = hzString.lowercase()
            return when {
                lower.contains("pink") -> NoiseColor.PINK
                lower.contains("white") -> NoiseColor.WHITE
                else -> NoiseColor.BROWN
            }
        }
    }
}
