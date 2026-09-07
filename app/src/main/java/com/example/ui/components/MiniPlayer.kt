package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FrequencyPreset
import com.example.ui.theme.AetheriaError
import com.example.ui.theme.AetheriaOnPrimary
import com.example.ui.theme.AetheriaOnSurface
import com.example.ui.theme.AetheriaOnSurfaceVariant
import com.example.ui.theme.AetheriaPrimary
import com.example.ui.theme.AetheriaSecondary
import com.example.ui.theme.AetheriaSurfaceContainer
import com.example.ui.theme.AetheriaSurfaceContainerHigh
import com.example.ui.theme.AetheriaSurfaceVariant
import com.example.ui.theme.AetheriaTertiary

@Composable
fun MiniPlayer(
    preset: FrequencyPreset,
    isPlaying: Boolean,
    onPlayPauseClick: () -> Unit,
    onExpandClick: () -> Unit,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
    onFavoriteToggle: (() -> Unit)? = null
) {
    // 4 Animated equalizer bars
    val infiniteTransition = rememberInfiniteTransition(label = "MiniWaveAnimation")

    val bar1Height by infiniteTransition.animateFloat(
        initialValue = 4f,
        targetValue = 16f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bar1"
    )

    val bar2Height by infiniteTransition.animateFloat(
        initialValue = 14f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(450, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bar2"
    )

    val bar3Height by infiniteTransition.animateFloat(
        initialValue = 6f,
        targetValue = 18f,
        animationSpec = infiniteRepeatable(
            animation = tween(750, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bar3"
    )

    val bar4Height by infiniteTransition.animateFloat(
        initialValue = 12f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bar4"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onExpandClick)
            .testTag("mini_player"),
        shape = RoundedCornerShape(18.dp),
        color = AetheriaSurfaceContainer.copy(alpha = 0.94f),
        tonalElevation = 8.dp,
        shadowElevation = 10.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Animated 4-bar equalizer thumbnail
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(AetheriaSurfaceContainerHigh),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .height(20.dp)
                            .padding(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.5.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .height(if (isPlaying) bar1Height.dp else 4.dp)
                                .clip(CircleShape)
                                .background(AetheriaPrimary)
                        )
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .height(if (isPlaying) bar2Height.dp else 6.dp)
                                .clip(CircleShape)
                                .background(AetheriaSecondary)
                        )
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .height(if (isPlaying) bar3Height.dp else 5.dp)
                                .clip(CircleShape)
                                .background(AetheriaTertiary)
                        )
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .height(if (isPlaying) bar4Height.dp else 4.dp)
                                .clip(CircleShape)
                                .background(AetheriaPrimary)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Title and details
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = preset.title,
                        color = AetheriaOnSurface,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${preset.category} • ${preset.technical_hz}",
                        color = AetheriaOnSurfaceVariant,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 1.dp)
                    )
                }

                // Action icons: Favorite + Play/Pause Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (onFavoriteToggle != null) {
                        IconButton(
                            onClick = onFavoriteToggle,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (isFavorite) AetheriaError else AetheriaOnSurfaceVariant,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    // Large Glowing Play / Pause Button
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(AetheriaPrimary)
                            .clickable(onClick = onPlayPauseClick)
                            .testTag("mini_player_play_pause"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = AetheriaOnPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            // Subtle glowing bottom progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(AetheriaSurfaceVariant.copy(alpha = 0.5f))
            ) {
                if (isPlaying) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.55f)
                            .height(2.dp)
                            .background(AetheriaSecondary)
                    )
                }
            }
        }
    }
}
