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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.vector.ImageVector
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

/**
 * One-time onboarding flow with 3 curated screens:
 * 1. App Purpose: Real-time on-device psychoacoustics & pure frequency synthesis.
 * 2. Headphone Recommendation: The science of binaural beats & stereo separation.
 * 3. Navigation Tips: How to explore, control playback, expand the visualizer, and set sleep timers.
 */
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
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Bar: Brand Pill + Step Indicator + Skip Button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
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
                            text = "AETHERIA",
                            color = AetheriaTertiary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )
                        Text(
                            text = "•",
                            color = AetheriaOutline,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "${currentSlide + 1} of 3",
                            color = AetheriaOnSurfaceVariant,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
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
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    AnimatedContent(
                        targetState = currentSlide,
                        transitionSpec = { fadeIn(tween(260)) togetherWith fadeOut(tween(260)) },
                        label = "OnboardingSlideAnimation",
                        modifier = Modifier.fillMaxSize()
                    ) { slide ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            when (slide) {
                                0 -> SlideOneAppPurpose()
                                1 -> SlideTwoHeadphoneRecommendation()
                                else -> SlideThreeNavigationTips()
                            }
                        }
                    }
                }

                // Bottom Controls: Pagination Dots + Navigation Action Buttons
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Pagination Dots (Interactive)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (i in 0..2) {
                            val isSelected = currentSlide == i
                            val dotColor = when (i) {
                                0 -> AetheriaPrimary
                                1 -> AetheriaSecondary
                                else -> AetheriaTertiary
                            }
                            Box(
                                modifier = Modifier
                                    .size(width = if (isSelected) 30.dp else 8.dp, height = 8.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) dotColor else AetheriaSurfaceVariant)
                                    .clickable { currentSlide = i }
                                    .testTag("onboarding_dot_$i")
                            )
                        }
                    }

                    // Action Buttons Row: [Back Button if not slide 0] + [Primary Next / Finish Button]
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (currentSlide > 0) {
                            Surface(
                                onClick = { currentSlide -= 1 },
                                shape = CircleShape,
                                color = AetheriaSurfaceContainerHigh,
                                modifier = Modifier
                                    .size(54.dp)
                                    .testTag("onboarding_prev_button")
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Previous",
                                        tint = AetheriaOnSurface,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }

                        // Primary Action Button
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(54.dp)
                                .clip(CircleShape)
                                .background(AetheriaCtaGradient)
                                .clickable {
                                    if (currentSlide < 2) {
                                        currentSlide += 1
                                    } else {
                                        onDismiss()
                                    }
                                }
                                .testTag(if (currentSlide == 2) "onboarding_finish_button" else "onboarding_next_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = if (currentSlide == 2) "Enter Sanctuary" else "Continue",
                                    color = AetheriaOnPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Icon(
                                    imageVector = if (currentSlide == 2) Icons.Default.Check else Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = AetheriaOnPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    // Secondary Science Guide link
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Text(
                            text = "Learn more in Psychoacoustic Science",
                            color = AetheriaOutline,
                            fontSize = 11.sp,
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
                    text = "Binaural beats occur when two tones of slightly different frequencies are presented separately to each ear. The human brain perceives a rhythmic beating tone corresponding to the frequency difference, synchronizing neural oscillations into targeted brainwave states (Delta, Theta, Alpha, Beta, or Gamma).\n\nAetheria synthesizes continuous mathematical sine waves directly on your device's audio hardware, delivering lossless, endless acoustic resonance without any static audio files.",
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
                    Text("Got It", color = AetheriaOnPrimary)
                }
            }
        )
    }
}

/**
 * Slide 1: App Purpose
 * Explaining real-time generative frequency synthesis, brainwave entrainment, and solfeggio healing.
 */
@Composable
private fun SlideOneAppPurpose() {
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
            .background(AetheriaSurfaceContainerLowest)
            .testTag("onboarding_slide_0"),
        contentAlignment = Alignment.Center
    ) {
        // Atmospheric gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            AetheriaPrimary.copy(alpha = 0.14f),
                            Color.Transparent,
                            AetheriaSurfaceContainerLowest
                        )
                    )
                )
        )

        // Concentric Frequency Rings
        Box(
            modifier = Modifier
                .size(230.dp)
                .scale(pulseScale),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(210.dp)
                    .clip(CircleShape)
                    .background(AetheriaPrimary.copy(alpha = 0.06f))
            )
            Box(
                modifier = Modifier
                    .size(165.dp)
                    .clip(CircleShape)
                    .background(AetheriaSurfaceContainerHigh.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(AetheriaPrimaryContainer.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
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
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }

        // Floating Frequency Badges
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 18.dp)
                .clip(CircleShape)
                .background(AetheriaSurfaceContainerHigh)
                .padding(horizontal = 10.dp, vertical = 5.dp),
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
                text = "528 Hz Healing",
                color = AetheriaSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 16.dp, start = 18.dp)
                .clip(CircleShape)
                .background(AetheriaSurfaceContainerHigh)
                .padding(horizontal = 10.dp, vertical = 5.dp),
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
                text = "Theta 6.0 Hz Deep State",
                color = AetheriaTertiary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

    Spacer(modifier = Modifier.height(20.dp))

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
            modifier = Modifier.size(13.dp)
        )
        Text(
            text = "PURE FREQUENCY SYNTHESIS",
            color = AetheriaPrimaryFixed,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }

    Spacer(modifier = Modifier.height(10.dp))

    Text(
        text = "Harmonize Mind & Body",
        color = AetheriaOnSurface,
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        letterSpacing = (-0.5).sp
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = "Aetheria generates real-time, mathematically pure acoustic frequencies directly on your device. Shift your brainwave states across Delta, Theta, Alpha, Beta, and Gamma with zero loops or static recordings.",
        color = AetheriaOnSurfaceVariant,
        fontSize = 13.sp,
        lineHeight = 19.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 8.dp)
    )
}

/**
 * Slide 2: Headphone Recommendation
 * Explaining why stereo separation is essential for binaural beats and brainwave entrainment.
 */
@Composable
private fun SlideTwoHeadphoneRecommendation() {
    val infiniteTransition = rememberInfiniteTransition(label = "BinauralPulse")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "binaural_pulse"
    )

    // Visual Stereo Representation
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(4f / 3f)
            .clip(RoundedCornerShape(24.dp))
            .background(AetheriaSurfaceContainerLowest)
            .testTag("onboarding_slide_1"),
        contentAlignment = Alignment.Center
    ) {
        // Ambient chromatic gradient aura
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        listOf(
                            AetheriaSecondary.copy(alpha = 0.16f),
                            AetheriaPrimary.copy(alpha = 0.08f),
                            Color.Transparent
                        )
                    )
                )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Ear Frequency Node
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
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

            // Central Headphone Entrainment Core
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.scale(pulse)
            ) {
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .background(AetheriaSurfaceContainerHighest),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Headphones,
                        contentDescription = null,
                        tint = AetheriaSecondary,
                        modifier = Modifier.size(40.dp)
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
                        text = "Δ 4.0 Hz Delta Wave",
                        color = AetheriaTertiary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Right Ear Frequency Node
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
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

    Spacer(modifier = Modifier.height(20.dp))

    // Badge Pill
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
            modifier = Modifier.size(13.dp)
        )
        Text(
            text = "STEREO HEADPHONES RECOMMENDED",
            color = AetheriaSecondaryFixed,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }

    Spacer(modifier = Modifier.height(10.dp))

    Text(
        text = "Use Stereo Headphones",
        color = AetheriaOnSurface,
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        letterSpacing = (-0.5).sp
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = "Binaural beats require independent stereo channels. When two frequencies enter each ear separately, your brain computes the difference to synchronize cognitive states. (Solfeggio tones and noise work great on phone speakers!).",
        color = AetheriaOnSurfaceVariant,
        fontSize = 13.sp,
        lineHeight = 19.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 8.dp)
    )
}

/**
 * Slide 3: Navigation Tips
 * Explaining Sanctuary, Explorer, Floating Mini-Player, and Sleep Timer / Library.
 */
@Composable
private fun SlideThreeNavigationTips() {
    // Navigation Tip Feature Grid
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(AetheriaSurfaceContainerLowest)
            .padding(14.dp)
            .testTag("onboarding_slide_2"),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        NavigationTipCard(
            icon = Icons.Default.Explore,
            iconColor = AetheriaPrimary,
            title = "Sanctuary & Explorer",
            description = "Browse circadian recommendations on Home, or filter by exact brainwave bands (Delta to Gamma) in Search."
        )

        NavigationTipCard(
            icon = Icons.Default.Tune,
            iconColor = AetheriaSecondary,
            title = "Sacred Geometry Player",
            description = "Tap any track to start listening. Tap the floating mini-player bar to view rotating geometry & multi-layer sliders."
        )

        NavigationTipCard(
            icon = Icons.Default.NightsStay,
            iconColor = AetheriaTertiary,
            title = "Sleep Timer & Library",
            description = "Set a 15–60 min auto fade-out timer for bedtime, and tap the heart icon on any preset to save it to your library."
        )
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Badge Pill
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(AetheriaSurfaceContainer)
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Tune,
            contentDescription = null,
            tint = AetheriaTertiary,
            modifier = Modifier.size(13.dp)
        )
        Text(
            text = "INTUITIVE CONTROLS & GESTURES",
            color = AetheriaTertiary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }

    Spacer(modifier = Modifier.height(10.dp))

    Text(
        text = "Seamless Flow & Controls",
        color = AetheriaOnSurface,
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        letterSpacing = (-0.5).sp
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = "Switch tracks seamlessly, customize master intensity and noise mixtures on the fly, and collapse the player to keep exploring while listening.",
        color = AetheriaOnSurfaceVariant,
        fontSize = 13.sp,
        lineHeight = 19.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 8.dp)
    )
}

@Composable
private fun NavigationTipCard(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    description: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = AetheriaSurfaceContainer
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = AetheriaOnSurface,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    color = AetheriaOnSurfaceVariant,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }
        }
    }
}
