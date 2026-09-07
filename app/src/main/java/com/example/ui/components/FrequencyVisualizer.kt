package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.PurpleAccent
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun FrequencyVisualizer(
    amplitude: Float,
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    primaryColor: Color = CyanAccent,
    secondaryColor: Color = PurpleAccent
) {
    val infiniteTransition = rememberInfiniteTransition(label = "VisualizerTransition")

    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "WavePhase"
    )

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseScale"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val centerX = width / 2f
        val centerY = height / 2f
        val baseRadius = minOf(width, height) * 0.32f

        // Dynamic amplitude multiplier
        val activeAmp = if (isPlaying) (amplitude.coerceIn(0.12f, 1.0f) * 1.6f) else 0.08f

        // 1. Draw outer ambient glowing aura
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    secondaryColor.copy(alpha = 0.28f * activeAmp),
                    primaryColor.copy(alpha = 0.12f * activeAmp),
                    Color.Transparent
                ),
                center = Offset(centerX, centerY),
                radius = baseRadius * 1.8f * pulseScale
            ),
            radius = baseRadius * 1.8f * pulseScale,
            center = Offset(centerX, centerY)
        )

        // 2. Draw harmonic concentric resonance rings
        val ringCount = 4
        for (i in 1..ringCount) {
            val ringRadius = baseRadius * (0.35f + (i * 0.2f)) * (if (isPlaying) pulseScale else 1.0f)
            val ringAlpha = (0.45f - (i * 0.08f)).coerceIn(0.1f, 0.6f) * (if (isPlaying) 1.0f else 0.4f)

            drawCircle(
                color = if (i % 2 == 0) primaryColor.copy(alpha = ringAlpha) else secondaryColor.copy(alpha = ringAlpha),
                radius = ringRadius,
                center = Offset(centerX, centerY),
                style = Stroke(width = if (i == 1) 2.5f else 1.5f)
            )
        }

        // 3. Draw flowing center sine waveform
        val path = Path()
        val waveWidth = width * 0.82f
        val startX = (width - waveWidth) / 2f
        val points = 80
        val waveHeight = 42f * activeAmp

        for (p in 0..points) {
            val fraction = p / points.toFloat()
            val x = startX + fraction * waveWidth
            // Window envelope so ends taper gracefully to 0
            val window = sin(fraction * PI.toFloat())
            val y = centerY + sin((fraction * 4 * PI.toFloat()) + wavePhase) * waveHeight * window

            if (p == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        drawPath(
            path = path,
            brush = Brush.horizontalGradient(
                colors = listOf(
                    primaryColor.copy(alpha = 0.2f),
                    primaryColor,
                    secondaryColor,
                    primaryColor.copy(alpha = 0.2f)
                ),
                startX = startX,
                endX = startX + waveWidth
            ),
            style = Stroke(width = 3.5f)
        )

        // 4. Center pulsing harmonic core
        val coreRadius = (16f + (activeAmp * 12f)) * pulseScale
        drawCircle(
            color = primaryColor,
            radius = coreRadius,
            center = Offset(centerX, centerY)
        )
        drawCircle(
            color = Color.White.copy(alpha = 0.85f),
            radius = coreRadius * 0.45f,
            center = Offset(centerX, centerY)
        )
    }
}
