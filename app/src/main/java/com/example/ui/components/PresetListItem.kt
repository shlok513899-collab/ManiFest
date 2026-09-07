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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Waves
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
import com.example.ui.theme.AetheriaOnPrimaryContainer
import com.example.ui.theme.AetheriaOnSurface
import com.example.ui.theme.AetheriaOnSurfaceVariant
import com.example.ui.theme.AetheriaOutline
import com.example.ui.theme.AetheriaPrimary
import com.example.ui.theme.AetheriaPrimaryContainer
import com.example.ui.theme.AetheriaSecondary
import com.example.ui.theme.AetheriaSurfaceContainer
import com.example.ui.theme.AetheriaSurfaceContainerHigh
import com.example.ui.theme.AetheriaSurfaceContainerHighest
import com.example.ui.theme.AetheriaSurfaceContainerLow
import com.example.ui.theme.AetheriaTertiary
import com.example.ui.theme.getAudioTypeColor

@Composable
fun PresetListItem(
    preset: FrequencyPreset,
    isPlaying: Boolean,
    isCurrent: Boolean,
    isFavorite: Boolean,
    onPresetClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier,
    onMoreClick: (() -> Unit)? = null
) {
    val typeColor = getAudioTypeColor(preset.audio_type)

    // Animated vertical wave heights for current playing track
    val infiniteTransition = rememberInfiniteTransition(label = "ItemEqualizer")
    val bar1 by infiniteTransition.animateFloat(
        initialValue = 4f, targetValue = 18f,
        animationSpec = infiniteRepeatable(tween(550, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "b1"
    )
    val bar2 by infiniteTransition.animateFloat(
        initialValue = 16f, targetValue = 6f,
        animationSpec = infiniteRepeatable(tween(420, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "b2"
    )
    val bar3 by infiniteTransition.animateFloat(
        initialValue = 6f, targetValue = 20f,
        animationSpec = infiniteRepeatable(tween(700, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "b3"
    )
    val bar4 by infiniteTransition.animateFloat(
        initialValue = 14f, targetValue = 5f,
        animationSpec = infiniteRepeatable(tween(480, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "b4"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onPresetClick)
            .testTag("preset_row_${preset.title.replace(" ", "_")}"),
        shape = RoundedCornerShape(16.dp),
        color = if (isCurrent) AetheriaSurfaceContainer else AetheriaSurfaceContainerLow,
        shadowElevation = if (isCurrent) 4.dp else 1.dp
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Left vertical active stripe
            if (isCurrent) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(52.dp)
                        .align(Alignment.CenterStart)
                        .clip(RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                        .background(AetheriaPrimary)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Leading Thumbnail / Equalizer Box
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isCurrent) AetheriaSurfaceContainerHighest else AetheriaSurfaceContainerHigh
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCurrent && isPlaying) {
                        // Animated 4-bar equalizer
                        Row(
                            modifier = Modifier
                                .height(22.dp)
                                .padding(horizontal = 3.dp),
                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(3.dp)
                                    .height(bar1.dp)
                                    .clip(CircleShape)
                                    .background(AetheriaPrimary)
                            )
                            Box(
                                modifier = Modifier
                                    .width(3.dp)
                                    .height(bar2.dp)
                                    .clip(CircleShape)
                                    .background(AetheriaPrimary)
                            )
                            Box(
                                modifier = Modifier
                                    .width(3.dp)
                                    .height(bar3.dp)
                                    .clip(CircleShape)
                                    .background(AetheriaSecondary)
                            )
                            Box(
                                modifier = Modifier
                                    .width(3.dp)
                                    .height(bar4.dp)
                                    .clip(CircleShape)
                                    .background(AetheriaPrimary)
                            )
                        }
                    } else {
                        // Thematic icon
                        val icon = when {
                            preset.audio_type.contains("Binaural", ignoreCase = true) -> Icons.Default.Headphones
                            preset.audio_type.contains("Solfeggio", ignoreCase = true) -> Icons.Default.Waves
                            preset.audio_type.contains("Noise", ignoreCase = true) -> Icons.Default.WaterDrop
                            preset.audio_type.contains("Chakra", ignoreCase = true) -> Icons.Default.SelfImprovement
                            preset.category.contains("Sleep", ignoreCase = true) -> Icons.Default.NightsStay
                            else -> Icons.Default.Spa
                        }
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (isCurrent) AetheriaPrimary else typeColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Track Details
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = preset.title,
                            color = if (isCurrent) AetheriaPrimary else AetheriaOnSurface,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )

                        // Audio type badge
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(
                                    if (isCurrent) AetheriaPrimaryContainer else typeColor.copy(alpha = 0.18f)
                                )
                                .padding(horizontal = 7.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = preset.audio_type,
                                color = if (isCurrent) AetheriaOnPrimaryContainer else typeColor,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Text(
                        text = preset.target_outcome,
                        color = AetheriaOnSurfaceVariant,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (isCurrent && isPlaying) {
                            Icon(
                                imageVector = Icons.Default.Equalizer,
                                contentDescription = null,
                                tint = AetheriaPrimary,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "Now Playing",
                                color = AetheriaPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "•",
                                color = AetheriaOutline,
                                fontSize = 10.sp
                            )
                        }

                        Text(
                            text = preset.technical_hz,
                            color = AetheriaOutline,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Trailing actions: Favorite + More options
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    IconButton(
                        onClick = onFavoriteToggle,
                        modifier = Modifier
                            .size(40.dp)
                            .testTag("favorite_btn_${preset.title.replace(" ", "_")}")
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                            tint = if (isFavorite) AetheriaError else AetheriaOnSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = { onMoreClick?.invoke() },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More",
                            tint = AetheriaOutline,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
