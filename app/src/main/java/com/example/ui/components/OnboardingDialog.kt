package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.AetheriaBackground
import com.example.ui.theme.AetheriaCtaGradient
import com.example.ui.theme.AetheriaOnPrimary
import com.example.ui.theme.AetheriaOnSurface
import com.example.ui.theme.AetheriaOnSurfaceVariant
import com.example.ui.theme.AetheriaOutline
import com.example.ui.theme.AetheriaPrimary
import com.example.ui.theme.AetheriaPrimaryContainer
import com.example.ui.theme.AetheriaPrimaryFixed
import com.example.ui.theme.AetheriaSecondary
import com.example.ui.theme.AetheriaSecondaryFixed
import com.example.ui.theme.AetheriaSurfaceContainer
import com.example.ui.theme.AetheriaSurfaceContainerHigh
import com.example.ui.theme.AetheriaSurfaceContainerHighest
import com.example.ui.theme.AetheriaSurfaceContainerLow
import com.example.ui.theme.AetheriaSurfaceContainerLowest
import com.example.ui.theme.AetheriaSurfaceVariant
import com.example.ui.theme.AetheriaTertiary

@Composable
fun OnboardingDialog(
    onDismiss: () -> Unit
) {
    var currentSlide by remember { mutableIntStateOf(0) }
    var showScienceDialog by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .testTag("onboarding_dialog"),
            color = AetheriaBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Bar: Ambient Brand Pill & Skip
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Brand Pill
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(AetheriaSurfaceContainerLow)
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(AetheriaTertiary)
                        )
                        Text(
                            text = "AETHERIA RESONANCE",
                            color = AetheriaTertiary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )
                    }

                    // Skip Button
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("onboarding_skip_button")
                    ) {
                        Text(
                            text = "Skip",
                            color = AetheriaOnSurfaceVariant,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Main Slide Carousel Viewport
                AnimatedContent(
                    targetState = currentSlide,
                    transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) },
                    label = "OnboardingSlideAnimation",
                    modifier = Modifier.weight(1f)
                ) { slide ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (slide == 0) {
                            SlideOneHarmonize()
                        } else {
                            SlideTwoStereoCalibration()
                        }
                    }
                }

                // Bottom Controls: Dots + Action Stack
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Pagination Dots
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = if (currentSlide == 0) 32.dp else 8.dp, height = 8.dp)
                                .clip(CircleShape)
                                .background(if (currentSlide == 0) AetheriaPrimary else AetheriaSurfaceVariant)
                                .clickable { currentSlide = 0 }
                        )
                        Box(
                            modifier = Modifier
                                .size(width = if (currentSlide == 1) 32.dp else 8.dp, height = 8.dp)
                                .clip(CircleShape)
                                .background(if (currentSlide == 1) AetheriaSecondary else AetheriaSurfaceVariant)
                                .clickable { currentSlide = 1 }
                        )
                    }

                    // Primary Glowing CTA Pill Button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(CircleShape)
                            .background(AetheriaCtaGradient)
                            .clickable {
                                if (currentSlide == 0) {
                                    currentSlide = 1
                                } else {
                                    onDismiss()
                                }
                            }
                            .testTag("onboarding_cta_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = if (currentSlide == 0) "Continue" else "Get Started",
                                color = AetheriaOnPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = AetheriaOnPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Secondary Links
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Text(
                            text = "Full Audio Library",
                            color = AetheriaOutline,
                            fontSize = 12.sp,
                            modifier = Modifier.clickable { onDismiss() }
                        )
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(AetheriaSurfaceVariant)
                        )
                        Text(
                            text = "Audio Science",
                            color = AetheriaOutline,
                            fontSize = 12.sp,
                            modifier = Modifier.clickable { showScienceDialog = true }
                        )
                    }
                }
            }
        }
    }

    if (showScienceDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showScienceDialog = false },
            containerColor = AetheriaSurfaceContainer,
            title = {
                Text(
                    text = "Psychoacoustic Science",
                    color = AetheriaOnSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = "Binaural beats occur when two tones of slightly different frequencies are presented separately to each ear. The human brain perceives a rhythmic beating tone corresponding to the frequency difference, synchronizing neural oscillations into targeted brainwave states (Delta, Theta, Alpha, Beta, or Gamma).",
                    color = AetheriaOnSurfaceVariant,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = { showScienceDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = AetheriaPrimary)
                ) {
                    Text("Understood", color = AetheriaOnPrimary)
                }
            }
        )
    }
}

@Composable
private fun SlideOneHarmonize() {
    val infiniteTransition = rememberInfiniteTransition(label = "HarmonizePulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    // Visual Art Canvas
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(4f / 3f)
            .clip(RoundedCornerShape(24.dp))
            .background(AetheriaSurfaceContainerLowest),
        contentAlignment = Alignment.Center
    ) {
        // Atmospheric gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            AetheriaPrimary.copy(alpha = 0.12f),
                            Color.Transparent,
                            AetheriaSurfaceContainerLowest
                        )
                    )
                )
        )

        // Concentric Frequency Rings
        Box(
            modifier = Modifier
                .size(240.dp)
                .scale(pulseScale),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .clip(CircleShape)
                    .background(AetheriaPrimary.copy(alpha = 0.05f))
            )
            Box(
                modifier = Modifier
                    .size(175.dp)
                    .clip(CircleShape)
                    .background(AetheriaSurfaceContainerHigh.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape)
                        .background(AetheriaPrimaryContainer.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(AetheriaPrimary, AetheriaSecondary)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = null,
                            tint = AetheriaOnPrimary,
                            modifier = Modifier.size(34.dp)
                        )
                    }
                }
            }
        }

        // Floating Harmonic Nodes
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 18.dp, end = 22.dp)
                .clip(CircleShape)
                .background(AetheriaSurfaceContainerHigh)
                .padding(horizontal = 10.dp, vertical = 4.dp),
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
                text = "528 Hz",
                color = AetheriaSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 18.dp, start = 22.dp)
                .clip(CircleShape)
                .background(AetheriaSurfaceContainerHigh)
                .padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(AetheriaTertiary)
            )
            Text(
                text = "Theta 6.0 Hz",
                color = AetheriaTertiary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Typography & Descriptions
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(AetheriaSurfaceContainer)
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            tint = AetheriaPrimary,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = "ACOUSTIC CELLULAR HEALING",
            color = AetheriaPrimaryFixed,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }

    Spacer(modifier = Modifier.height(10.dp))

    Text(
        text = "Harmonize Mind & Body",
        color = AetheriaOnSurface,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        letterSpacing = (-0.5).sp
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = "Immerse into precision binaural beats, solfeggio tones, and psychoacoustic noise sculpted for lucid focus and deep restorative sleep.",
        color = AetheriaOnSurfaceVariant,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 12.dp)
    )
}

@Composable
private fun SlideTwoStereoCalibration() {
    // Stylized Dual-Chamber Resonator & Headphone Archetype
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(4f / 3f)
            .clip(RoundedCornerShape(24.dp))
            .background(AetheriaSurfaceContainerLowest),
        contentAlignment = Alignment.Center
    ) {
        // Ambient chromatic gradient aura
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        listOf(
                            AetheriaSecondary.copy(alpha = 0.15f),
                            AetheriaPrimary.copy(alpha = 0.08f),
                            Color.Transparent
                        )
                    )
                )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Ear
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(AetheriaSurfaceContainerHigh),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "210 Hz",
                        color = AetheriaSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "LEFT EAR",
                    color = AetheriaOnSurfaceVariant,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            // Central Headphone Node
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(AetheriaSurfaceContainerHighest),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Headphones,
                        contentDescription = null,
                        tint = AetheriaSecondary,
                        modifier = Modifier.size(44.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(AetheriaSurfaceContainer)
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "Δ 4.0 Hz Entrainment",
                        color = AetheriaTertiary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Right Ear
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(AetheriaSurfaceContainerHigh),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "214 Hz",
                        color = AetheriaTertiary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "RIGHT EAR",
                    color = AetheriaOnSurfaceVariant,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Typography & Descriptions
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(AetheriaSurfaceContainer)
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Headphones,
            contentDescription = null,
            tint = AetheriaSecondary,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = "TRUE STEREO ESSENTIAL",
            color = AetheriaSecondaryFixed,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }

    Spacer(modifier = Modifier.height(10.dp))

    Text(
        text = "Headphones Required",
        color = AetheriaOnSurface,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        letterSpacing = (-0.5).sp
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = "Binaural beats require stereo separation. Divergent ear frequencies synchronize hemispheres to calibrate delta, theta, and alpha states.",
        color = AetheriaOnSurfaceVariant,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 12.dp)
    )
}
