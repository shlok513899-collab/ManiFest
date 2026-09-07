package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.service.SleepTimerDuration
import com.example.ui.theme.AetheriaBackground
import com.example.ui.theme.AetheriaError
import com.example.ui.theme.AetheriaOnPrimary
import com.example.ui.theme.AetheriaOnSurface
import com.example.ui.theme.AetheriaOnSurfaceVariant
import com.example.ui.theme.AetheriaOutline
import com.example.ui.theme.AetheriaPrimary
import com.example.ui.theme.AetheriaPrimaryContainer
import com.example.ui.theme.AetheriaSecondary
import com.example.ui.theme.AetheriaSurfaceContainer
import com.example.ui.theme.AetheriaSurfaceContainerHigh
import com.example.ui.theme.AetheriaSurfaceContainerHighest
import com.example.ui.theme.AetheriaSurfaceVariant
import com.example.ui.theme.AetheriaTertiary
import com.example.ui.viewmodel.FrequencyPlayerViewModel

@Composable
fun PlayerScreen(
    viewModel: FrequencyPlayerViewModel,
    onCollapse: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentPreset by viewModel.currentPreset.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val amplitude by viewModel.amplitude.collectAsState()
    val masterVolume by viewModel.masterVolume.collectAsState()
    val toneVolume by viewModel.toneVolume.collectAsState()
    val noiseVolume by viewModel.noiseVolume.collectAsState()
    val isLooping by viewModel.isLooping.collectAsState()
    val sleepTimer by viewModel.sleepTimer.collectAsState()
    val sleepSecondsRemaining by viewModel.sleepSecondsRemaining.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()

    val preset = currentPreset ?: return
    val isFavorite = favoriteIds.contains(preset.id)

    // Sacred geometry rotating animation
    val infiniteTransition = rememberInfiniteTransition(label = "SacredGeometry")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(28000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    val ringPulse by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(3200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AetheriaBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("player_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header: Collapse Button + Title + Favorite Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onCollapse,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(AetheriaSurfaceContainerHigh)
                        .testTag("player_collapse_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Collapse Player",
                        tint = AetheriaOnSurface,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "DEEP STATE RESONANCE",
                        color = AetheriaPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.4.sp
                    )
                    Text(
                        text = preset.category,
                        color = AetheriaOnSurfaceVariant,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(
                    onClick = { viewModel.toggleFavorite(preset) },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(AetheriaSurfaceContainerHigh)
                        .testTag("player_favorite_button")
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) AetheriaError else AetheriaOnSurfaceVariant,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Central Sacred Geometry Visualizer Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(32.dp))
                    .background(AetheriaSurfaceContainer)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                // Background ambient blur
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .clip(CircleShape)
                        .background(AetheriaPrimary.copy(alpha = 0.15f))
                        .blur(36.dp)
                )

                // Rotating Sacred Concentric Harmonic Resonance Rings
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .rotate(if (isPlaying) rotationAngle else 0f)
                ) {
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val baseRadius = (size.minDimension / 2f) * 0.85f * ringPulse

                    // Outer dashed ring
                    drawCircle(
                        color = AetheriaPrimary.copy(alpha = 0.3f),
                        radius = baseRadius,
                        center = center,
                        style = Stroke(
                            width = 1.5.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f), 0f)
                        )
                    )

                    // Secondary solid ring
                    drawCircle(
                        color = AetheriaSecondary.copy(alpha = 0.35f),
                        radius = baseRadius * 0.76f,
                        center = center,
                        style = Stroke(width = 1.5.dp.toPx())
                    )

                    // Third dotted harmonic ring
                    drawCircle(
                        color = AetheriaTertiary.copy(alpha = 0.4f),
                        radius = baseRadius * 0.54f,
                        center = center,
                        style = Stroke(
                            width = 1.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 8f), 0f)
                        )
                    )

                    // Inner energy field
                    drawCircle(
                        color = AetheriaPrimary.copy(alpha = 0.15f),
                        radius = baseRadius * 0.34f,
                        center = center
                    )
                }

                // Center Live Frequency Readout Node
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Hz Readout
                    val hzValue = preset.technical_hz.split(" ").firstOrNull { it.contains(Regex("\\d+")) } ?: "432"
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = hzValue,
                            color = AetheriaOnSurface,
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-1).sp
                        )
                        Text(
                            text = "Hz",
                            color = AetheriaPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 6.dp, start = 2.dp)
                        )
                    }

                    Text(
                        text = preset.audio_type.uppercase(),
                        color = AetheriaSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )

                    Text(
                        text = preset.technical_hz,
                        color = AetheriaOnSurfaceVariant,
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Preset Title & Target Outcome Info
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Audio Type Badge Pill
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(AetheriaSurfaceContainerHigh)
                        .padding(horizontal = 12.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(AetheriaSecondary)
                    )
                    Text(
                        text = preset.audio_type,
                        color = AetheriaOnSurfaceVariant,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )

                    if (preset.toneConfig.isStereoHeadphonesRequired) {
                        Text(text = "•", color = AetheriaOutline, fontSize = 10.sp)
                        Icon(
                            imageVector = Icons.Default.Headphones,
                            contentDescription = "Headphones Required",
                            tint = AetheriaPrimary,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "Headphones",
                            color = AetheriaPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = preset.title,
                    color = AetheriaOnSurface,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = preset.target_outcome,
                    color = AetheriaOnSurfaceVariant,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Transport Controls Row (Loop, Prev, Large Play/Pause, Next, Sleep Timer)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Loop toggle
                IconButton(
                    onClick = { viewModel.toggleLoop() },
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("loop_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Repeat,
                        contentDescription = "Loop",
                        tint = if (isLooping) AetheriaPrimary else AetheriaOutline,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Previous
                IconButton(
                    onClick = { viewModel.playPrevious(context) },
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("previous_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = "Previous",
                        tint = AetheriaOnSurface,
                        modifier = Modifier.size(32.dp)
                    )
                }

                // Large Glowing Play/Pause Circular Button
                Surface(
                    onClick = { viewModel.togglePlayPause(context) },
                    shape = CircleShape,
                    color = AetheriaPrimary,
                    shadowElevation = 10.dp,
                    modifier = Modifier
                        .size(70.dp)
                        .testTag("player_play_pause_button")
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = AetheriaOnPrimary,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                // Next
                IconButton(
                    onClick = { viewModel.playNext(context) },
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("next_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Next",
                        tint = AetheriaOnSurface,
                        modifier = Modifier.size(32.dp)
                    )
                }

                // Sleep Timer
                IconButton(
                    onClick = { viewModel.setShowSleepTimerDialog(true) },
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("sleep_timer_button")
                ) {
                    Box(contentAlignment = Alignment.TopEnd) {
                        Icon(
                            imageVector = Icons.Default.NightsStay,
                            contentDescription = "Sleep Timer",
                            tint = if (sleepTimer != SleepTimerDuration.OFF) AetheriaSecondary else AetheriaOutline,
                            modifier = Modifier.size(24.dp)
                        )
                        if (sleepTimer != SleepTimerDuration.OFF) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(AetheriaSecondary)
                            )
                        }
                    }
                }
            }

            // Sleep timer status badge
            if (sleepTimer != SleepTimerDuration.OFF && sleepSecondsRemaining != null) {
                val mins = sleepSecondsRemaining!! / 60
                val secs = sleepSecondsRemaining!! % 60
                Text(
                    text = "Sleep timer: %02d:%02d".format(mins, secs),
                    color = AetheriaSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Master Volume & Layer Sliders Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = AetheriaSurfaceContainer
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = null,
                                tint = AetheriaPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Master Intensity",
                                color = AetheriaOnSurface,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Text(
                            text = "${(masterVolume * 100).toInt()}%",
                            color = AetheriaOnSurfaceVariant,
                            fontSize = 12.sp
                        )
                    }

                    Slider(
                        value = masterVolume,
                        onValueChange = { viewModel.setMasterVolume(it) },
                        valueRange = 0f..1f,
                        colors = SliderDefaults.colors(
                            thumbColor = AetheriaPrimary,
                            activeTrackColor = AetheriaPrimary,
                            inactiveTrackColor = AetheriaSurfaceVariant
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("master_volume_slider")
                    )

                    // Multi-layer sliders if noise or tone combination
                    if (preset.toneConfig.hasNoise && preset.toneConfig.hasTone) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Noise Layer: ${(noiseVolume * 100).toInt()}%",
                                    color = AetheriaOnSurfaceVariant,
                                    fontSize = 11.sp
                                )
                                Slider(
                                    value = noiseVolume,
                                    onValueChange = { viewModel.setNoiseVolume(it) },
                                    valueRange = 0f..1f,
                                    colors = SliderDefaults.colors(
                                        thumbColor = AetheriaSecondary,
                                        activeTrackColor = AetheriaSecondary,
                                        inactiveTrackColor = AetheriaSurfaceVariant
                                    ),
                                    modifier = Modifier.testTag("noise_mix_slider")
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Tone Layer: ${(toneVolume * 100).toInt()}%",
                                    color = AetheriaOnSurfaceVariant,
                                    fontSize = 11.sp
                                )
                                Slider(
                                    value = toneVolume,
                                    onValueChange = { viewModel.setToneVolume(it) },
                                    valueRange = 0f..1f,
                                    colors = SliderDefaults.colors(
                                        thumbColor = AetheriaTertiary,
                                        activeTrackColor = AetheriaTertiary,
                                        inactiveTrackColor = AetheriaSurfaceVariant
                                    ),
                                    modifier = Modifier.testTag("tone_mix_slider")
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
