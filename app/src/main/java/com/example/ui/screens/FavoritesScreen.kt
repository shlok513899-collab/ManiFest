package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SelfImprovement
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.PresetListItem
import com.example.ui.theme.AetheriaBackground
import com.example.ui.theme.AetheriaError
import com.example.ui.theme.AetheriaOnPrimary
import com.example.ui.theme.AetheriaOnSurface
import com.example.ui.theme.AetheriaOnSurfaceVariant
import com.example.ui.theme.AetheriaOutline
import com.example.ui.theme.AetheriaPrimary
import com.example.ui.theme.AetheriaSecondary
import com.example.ui.theme.AetheriaSurfaceContainer
import com.example.ui.theme.AetheriaSurfaceContainerHigh
import com.example.ui.theme.AetheriaSurfaceContainerLow
import com.example.ui.theme.AetheriaTertiary
import com.example.ui.viewmodel.FrequencyPlayerViewModel

@Composable
fun FavoritesScreen(
    viewModel: FrequencyPlayerViewModel,
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val allPresets by viewModel.presets.collectAsState()
    val currentPreset by viewModel.currentPreset.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()

    val favoritePresets = allPresets.filter { favoriteIds.contains(it.id) }

    var selectedTab by remember { mutableStateOf("All Saved") }
    val categoriesInFavs = listOf("All Saved") + favoritePresets.map { it.category.split(" ").first() }.distinct()

    val filteredList = if (selectedTab == "All Saved") {
        favoritePresets
    } else {
        favoritePresets.filter { it.category.contains(selectedTab, ignoreCase = true) }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AetheriaBackground)
            .statusBarsPadding()
            .testTag("favorites_screen")
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (onBack != null) {
                            IconButton(
                                onClick = onBack,
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(AetheriaSurfaceContainerHigh)
                                    .testTag("favorites_back_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = AetheriaOnSurface,
                                    modifier = Modifier.size(19.dp)
                                )
                            }
                        }

                        Column {
                            Text(
                                text = "Sacred Library",
                                color = AetheriaOnSurface,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = (-0.5).sp
                            )
                            Text(
                                text = "Your personal harmonic collection",
                                color = AetheriaOnSurfaceVariant,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(AetheriaSurfaceContainerHigh),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.BookmarkAdded,
                            contentDescription = null,
                            tint = AetheriaPrimary,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }
            }

            // Stats Cards Row: 2 Cards (Saved Tones, Total Entrainment)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Card 1: Saved Tones
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(96.dp)
                            .clip(RoundedCornerShape(18.dp)),
                        shape = RoundedCornerShape(18.dp),
                        color = AetheriaSurfaceContainer
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "SAVED TONES",
                                    color = AetheriaPrimary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = null,
                                    tint = AetheriaError,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = "${favoritePresets.size} Presets",
                                color = AetheriaOnSurface,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Card 2: Total Entrainment (estimated)
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(96.dp)
                            .clip(RoundedCornerShape(18.dp)),
                        shape = RoundedCornerShape(18.dp),
                        color = AetheriaSurfaceContainer
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "ENTRAINMENT",
                                    color = AetheriaSecondary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = AetheriaSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = "${favoritePresets.size * 45} min",
                                color = AetheriaOnSurface,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Category Filter Tabs
            if (favoritePresets.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        categoriesInFavs.forEach { tab ->
                            val isSelected = tab == selectedTab
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(if (isSelected) AetheriaPrimary else AetheriaSurfaceContainerHigh)
                                    .clickable { selectedTab = tab }
                                    .padding(horizontal = 14.dp, vertical = 7.dp)
                            ) {
                                Text(
                                    text = tab,
                                    color = if (isSelected) AetheriaOnPrimary else AetheriaOnSurfaceVariant,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // Presets List or Empty State
            if (favoritePresets.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(AetheriaSurfaceContainerHigh),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = null,
                                    tint = AetheriaError.copy(alpha = 0.6f),
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Your Library is Waiting",
                                color = AetheriaOnSurface,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Tap the heart icon on any frequency track to save it here for fast sacred sessions.",
                                color = AetheriaOnSurfaceVariant,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center,
                                lineHeight = 18.sp,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                        }
                    }
                }
            } else {
                items(filteredList, key = { it.id }) { preset ->
                    val isCurrent = currentPreset?.id == preset.id

                    PresetListItem(
                        preset = preset,
                        isPlaying = isPlaying,
                        isCurrent = isCurrent,
                        isFavorite = true,
                        onPresetClick = {
                            if (isCurrent) {
                                viewModel.togglePlayPause(context)
                            } else {
                                viewModel.playPreset(context, preset, filteredList)
                                viewModel.setPlayerExpanded(true)
                            }
                        },
                        onFavoriteToggle = {
                            viewModel.toggleFavorite(preset)
                        }
                    )
                }
            }

            // Bottom spacer
            item {
                Spacer(modifier = Modifier.height(140.dp))
            }
        }
    }
}
