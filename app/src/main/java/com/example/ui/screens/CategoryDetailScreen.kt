package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.PresetListItem
import com.example.ui.theme.AetheriaBackground
import com.example.ui.theme.AetheriaOnPrimary
import com.example.ui.theme.AetheriaOnSurface
import com.example.ui.theme.AetheriaOnSurfaceVariant
import com.example.ui.theme.AetheriaOutline
import com.example.ui.theme.AetheriaPrimary
import com.example.ui.theme.AetheriaPrimaryContainer
import com.example.ui.theme.AetheriaSecondary
import com.example.ui.theme.AetheriaSurfaceBright
import com.example.ui.theme.AetheriaSurfaceContainer
import com.example.ui.theme.AetheriaSurfaceContainerHigh
import com.example.ui.theme.AetheriaSurfaceContainerHighest
import com.example.ui.theme.AetheriaSurfaceContainerLow
import com.example.ui.theme.AetheriaTertiary
import com.example.ui.viewmodel.FrequencyPlayerViewModel

@Composable
fun CategoryDetailScreen(
    categoryName: String,
    viewModel: FrequencyPlayerViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val presets by viewModel.categoryPresets.collectAsState()
    val currentPreset by viewModel.currentPreset.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()

    var filterType by remember { mutableStateOf<String?>(null) }
    var sortByDuration by remember { mutableStateOf(false) }

    val displayedPresets = presets
        .filter { filterType == null || it.audio_type.contains(filterType!!, ignoreCase = true) }
        .let { list ->
            if (sortByDuration) list.sortedBy { it.title } else list
        }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AetheriaBackground)
            .statusBarsPadding()
            .testTag("category_detail_screen")
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Top Navigation & Action Row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(AetheriaSurfaceContainerHigh)
                            .testTag("category_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = AetheriaOnSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "ACOUSTIC SANCTUARY",
                            color = AetheriaPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = categoryName,
                            color = AetheriaOnSurface,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "Experience therapeutic wellness frequency: $categoryName on Aetheria.")
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Share Frequency"))
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(AetheriaSurfaceContainerHigh)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = AetheriaOnSurfaceVariant,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }
            }

            // Hero Category Banner Card with Celestial Rings
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp)),
                    shape = RoundedCornerShape(22.dp),
                    color = AetheriaSurfaceContainerLow,
                    shadowElevation = 6.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        // Ambient gradient glows
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .align(Alignment.TopEnd)
                                .clip(CircleShape)
                                .background(AetheriaPrimaryContainer.copy(alpha = 0.2f))
                                .blur(28.dp)
                        )

                        // Decorative Celestial Orbital Rings Canvas
                        Canvas(
                            modifier = Modifier
                                .size(120.dp)
                                .align(Alignment.TopEnd)
                        ) {
                            val center = Offset(size.width * 0.7f, size.height * 0.4f)
                            drawCircle(
                                color = Color(0xFF8B5CF6).copy(alpha = 0.35f),
                                radius = 46.dp.toPx(),
                                center = center,
                                style = Stroke(
                                    width = 1.dp.toPx(),
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                                )
                            )
                            drawCircle(
                                color = Color(0xFF06B6D4).copy(alpha = 0.4f),
                                radius = 32.dp.toPx(),
                                center = center,
                                style = Stroke(width = 1.2.dp.toPx())
                            )
                            drawCircle(
                                color = Color(0xFF10B981).copy(alpha = 0.35f),
                                radius = 18.dp.toPx(),
                                center = center,
                                style = Stroke(
                                    width = 1.dp.toPx(),
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                                )
                            )
                        }

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Tuning chip
                            Row(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(AetheriaSurfaceContainerHighest.copy(alpha = 0.7f))
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
                                    text = "432Hz Baseline Master Tuned",
                                    color = AetheriaOnSurfaceVariant,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Text(
                                text = when {
                                    categoryName.contains("Sleep", ignoreCase = true) -> "Delta Genesis"
                                    categoryName.contains("Focus", ignoreCase = true) -> "Beta Acceleration"
                                    categoryName.contains("Drive", ignoreCase = true) -> "Gamma Surge"
                                    else -> categoryName
                                },
                                color = AetheriaOnSurface,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Precision neuro-acoustic frequency field sculpted for cellular harmonic reset and somatic tranquility.",
                                color = AetheriaOnSurfaceVariant,
                                fontSize = 13.sp,
                                lineHeight = 18.sp,
                                modifier = Modifier.fillMaxWidth(0.75f)
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // Micro stats footnote
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Bedtime,
                                        contentDescription = null,
                                        tint = AetheriaPrimary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "0.5Hz – 3.9Hz",
                                        color = AetheriaOnSurfaceVariant,
                                        fontSize = 11.sp
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Spa,
                                        contentDescription = null,
                                        tint = AetheriaTertiary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "Cellular Sync",
                                        color = AetheriaOnSurfaceVariant,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Action Controls Row: Shuffle Play + Filter Tones
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Shuffle Play
                    Button(
                        onClick = {
                            val random = displayedPresets.shuffled().firstOrNull() ?: presets.firstOrNull()
                            random?.let {
                                viewModel.playPreset(context, it, presets)
                                viewModel.setPlayerExpanded(true)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AetheriaPrimary,
                            contentColor = AetheriaOnPrimary
                        ),
                        shape = CircleShape,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("shuffle_play_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shuffle,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Shuffle Play",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }

                    // Filter Tones
                    Button(
                        onClick = {
                            filterType = when (filterType) {
                                null -> "Binaural"
                                "Binaural" -> "Solfeggio"
                                "Solfeggio" -> "Noise"
                                else -> null
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AetheriaSurfaceContainerHigh,
                            contentColor = AetheriaOnSurface
                        ),
                        shape = CircleShape,
                        modifier = Modifier.height(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = null,
                            tint = AetheriaSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (filterType != null) filterType!! else "Filter Tones",
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(if (filterType != null) AetheriaSecondary else Color.Transparent)
                        )
                    }
                }
            }

            // Presets Header: "Curated Resonances" + Duration Sort
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Curated Resonances",
                            color = AetheriaOnSurface,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(AetheriaSurfaceContainerHigh)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "${displayedPresets.size} Presets",
                                color = AetheriaOnSurfaceVariant,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { sortByDuration = !sortByDuration }
                    ) {
                        Text(
                            text = if (sortByDuration) "Alphabetical" else "Duration",
                            color = AetheriaOnSurfaceVariant,
                            fontSize = 12.sp
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = AetheriaOnSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Presets Tracklist
            items(displayedPresets, key = { it.id }) { preset ->
                val isCurrent = currentPreset?.id == preset.id
                val isFav = favoriteIds.contains(preset.id)

                PresetListItem(
                    preset = preset,
                    isPlaying = isPlaying,
                    isCurrent = isCurrent,
                    isFavorite = isFav,
                    onPresetClick = {
                        if (isCurrent) {
                            viewModel.togglePlayPause(context)
                        } else {
                            viewModel.playPreset(context, preset, presets)
                            viewModel.setPlayerExpanded(true)
                        }
                    },
                    onFavoriteToggle = {
                        viewModel.toggleFavorite(preset)
                    }
                )
            }

            // Tip Callout Card at bottom
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp)),
                    shape = RoundedCornerShape(18.dp),
                    color = AetheriaSurfaceContainerHigh.copy(alpha = 0.6f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(AetheriaPrimary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Headphones,
                                contentDescription = null,
                                tint = AetheriaPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "Binaural Acoustic Tip",
                                color = AetheriaOnSurface,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Wear stereo headphones or place dual nightstand speakers at ear level to engage the hemisphere synchrony effect.",
                                color = AetheriaOnSurfaceVariant,
                                fontSize = 12.sp,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
            }

            // Spacer for mini-player
            item {
                Spacer(modifier = Modifier.height(140.dp))
            }
        }
    }
}
