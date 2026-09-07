package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val AetheriaDarkColorScheme = darkColorScheme(
    primary = AetheriaPrimary,
    onPrimary = AetheriaOnPrimary,
    primaryContainer = AetheriaPrimaryContainer,
    onPrimaryContainer = AetheriaOnPrimaryContainer,
    secondary = AetheriaSecondary,
    onSecondary = AetheriaOnSecondary,
    secondaryContainer = AetheriaSecondaryContainer,
    onSecondaryContainer = AetheriaOnSecondaryContainer,
    tertiary = AetheriaTertiary,
    onTertiary = AetheriaOnTertiary,
    tertiaryContainer = AetheriaTertiaryContainer,
    background = AetheriaBackground,
    onBackground = AetheriaOnSurface,
    surface = AetheriaSurface,
    onSurface = AetheriaOnSurface,
    surfaceVariant = AetheriaSurfaceVariant,
    onSurfaceVariant = AetheriaOnSurfaceVariant,
    outline = AetheriaOutline,
    outlineVariant = AetheriaOutlineVariant,
    error = AetheriaError,
    onError = Color(0xFF690005)
)

@Composable
fun ResonanceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = AetheriaDarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = AetheriaBackground.toArgb()
            window.navigationBarColor = AetheriaBackground.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
