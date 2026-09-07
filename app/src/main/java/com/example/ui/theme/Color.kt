package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Aetheria Cosmic Sanctuary Theme Palette (Direct from HTML Design)
val AetheriaBackground = Color(0xFF0F131D)
val AetheriaSurface = Color(0xFF0F131D)
val AetheriaSurfaceDim = Color(0xFF0F131D)
val AetheriaSurfaceContainerLowest = Color(0xFF0A0E18)
val AetheriaSurfaceContainerLow = Color(0xFF171B26)
val AetheriaSurfaceContainer = Color(0xFF1C1F2A)
val AetheriaSurfaceContainerHigh = Color(0xFF262A35)
val AetheriaSurfaceContainerHighest = Color(0xFF313540)
val AetheriaSurfaceBright = Color(0xFF353944)
val AetheriaSurfaceVariant = Color(0xFF313540)

// Brand Core Accents
val AetheriaPrimary = Color(0xFFD0BCFF) // Soft Lavender Violet
val AetheriaPrimaryContainer = Color(0xFFA078FF)
val AetheriaPrimaryFixed = Color(0xFFE9DDFF)
val AetheriaPrimaryFixedDim = Color(0xFFD0BCFF)
val AetheriaOnPrimary = Color(0xFF3C0091)
val AetheriaOnPrimaryContainer = Color(0xFF340080)

val AetheriaSecondary = Color(0xFF4CD7F6) // Luminescent Cyan
val AetheriaSecondaryContainer = Color(0xFF03B5D3)
val AetheriaSecondaryFixed = Color(0xFFACEDFF)
val AetheriaOnSecondary = Color(0xFF003640)
val AetheriaOnSecondaryContainer = Color(0xFF00424E)

val AetheriaTertiary = Color(0xFF4EDEA3) // Bio-Acoustic Emerald
val AetheriaTertiaryContainer = Color(0xFF00A572)
val AetheriaTertiaryFixed = Color(0xFF6FFBBE)
val AetheriaOnTertiary = Color(0xFF003824)

// Text & Boundary
val AetheriaOnSurface = Color(0xFFDFE2F1)
val AetheriaOnSurfaceVariant = Color(0xFFCBC3D7)
val AetheriaOutline = Color(0xFF958EA0)
val AetheriaOutlineVariant = Color(0xFF494454)
val AetheriaError = Color(0xFFFFB4AB)
val AetheriaErrorContainer = Color(0xFF93000A)

// Compatibility aliases
val DeepTwilightBackground = AetheriaBackground
val DeepTwilightSurface = AetheriaSurfaceContainer
val DeepTwilightSurfaceVariant = AetheriaSurfaceContainerHigh
val DeepTwilightCard = AetheriaSurfaceContainer

val CyanAccent = AetheriaSecondary
val TealAccent = Color(0xFF14B8A6)
val PurpleAccent = AetheriaPrimary
val IndigoAccent = Color(0xFF8B5CF6)
val AmberAccent = Color(0xFFFED37F)
val OrangeAccent = Color(0xFFF97316)
val EmeraldAccent = AetheriaTertiary
val RoseAccent = Color(0xFFF43F5E)
val OchreBrown = Color(0xFFB45309)

val TextPrimary = AetheriaOnSurface
val TextSecondary = AetheriaOnSurfaceVariant
val TextMuted = AetheriaOutline

// Gradients
val AetheriaLogoGradient = Brush.linearGradient(
    listOf(Color(0xFF8B5CF6), Color(0xFF06B6D4), Color(0xFF10B981))
)
val AetheriaCtaGradient = Brush.horizontalGradient(
    listOf(Color(0xFFA078FF), Color(0xFFD0BCFF), Color(0xFF4CD7F6))
)

// Functional & Gradients
fun getCategoryGradient(category: String): Brush {
    val lower = category.lowercase()
    return when {
        lower.contains("drive") || lower.contains("motivation") || lower.contains("athletic") || lower.contains("energy") -> {
            Brush.verticalGradient(listOf(Color(0xFF2A1505), Color(0xFF090D16)))
        }
        lower.contains("sleep") || lower.contains("rest") || lower.contains("night") -> {
            Brush.verticalGradient(listOf(Color(0xFF061426), Color(0xFF090D16)))
        }
        lower.contains("calm") || lower.contains("anxiety") || lower.contains("stress") || lower.contains("nature") -> {
            Brush.verticalGradient(listOf(Color(0xFF06231E), Color(0xFF090D16)))
        }
        lower.contains("chakra") || lower.contains("meditation") || lower.contains("manifestation") || lower.contains("angel") -> {
            Brush.verticalGradient(listOf(Color(0xFF200F2E), Color(0xFF090D16)))
        }
        lower.contains("love") || lower.contains("romance") || lower.contains("sensuality") || lower.contains("feminine") -> {
            Brush.verticalGradient(listOf(Color(0xFF2E0D1B), Color(0xFF090D16)))
        }
        lower.contains("focus") || lower.contains("clarity") || lower.contains("memory") -> {
            Brush.verticalGradient(listOf(Color(0xFF0D1E36), Color(0xFF090D16)))
        }
        else -> {
            Brush.verticalGradient(listOf(Color(0xFF151C2C), Color(0xFF090D16)))
        }
    }
}

fun getAudioTypeColor(audioType: String): Color {
    val lower = audioType.lowercase()
    return when {
        lower.contains("binaural") -> CyanAccent
        lower.contains("solfeggio") -> PurpleAccent
        lower.contains("chakra") -> IndigoAccent
        lower.contains("isochronic") -> OrangeAccent
        lower.contains("colored noise") || lower.contains("noise") -> OchreBrown
        lower.contains("angel") -> Color(0xFFE879F9)
        lower.contains("planetary") -> AmberAccent
        lower.contains("harmonic") -> TealAccent
        lower.contains("resonant") -> EmeraldAccent
        else -> CyanAccent
    }
}
