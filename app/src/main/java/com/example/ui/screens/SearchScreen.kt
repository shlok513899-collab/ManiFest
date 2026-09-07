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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Tune
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.PresetListItem
import com.example.ui.theme.AetheriaBackground
import com.example.ui.theme.AetheriaOnPrimary
import com.example.ui.theme.AetheriaOnSurface
import com.example.ui.theme.AetheriaOnSurfaceVariant
import com.example.ui.theme.AetheriaOutline
import com.example.ui.theme.AetheriaPrimary
import com.example.ui.theme.AetheriaSecondary
import com.example.ui.theme.AetheriaSurfaceContainer
import com.example.ui.theme.AetheriaSurfaceContainerHigh
import com.example.ui.theme.AetheriaSurfaceContainerHighest
import com.example.ui.theme.AetheriaSurfaceContainerLow
import com.example.ui.theme.AetheriaTertiary
import com.example.ui.viewmodel.FrequencyPlayerViewModel

data class QuickOutcomeCard(
    val title: String,
    val range: String,
    val icon: ImageVector,
    val color: Color,
    val queryTerm: String
)

@Composable
fun SearchScreen(
    viewModel: FrequencyPlayerViewModel,
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val query by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val allPresets by viewModel.presets.collectAsState()
    val currentPreset by viewModel.currentPreset.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()

    var selectedFilter by remember { mutableStateOf("All Frequencies") }
    val filterTabs = listOf(
        "All Frequencies",
        "0.5-4Hz (Delta)",
        "4-8Hz (Theta)",
        "8-12Hz (Alpha)",
        "12-30Hz (Beta)",
        "30-100Hz (Gamma)",
        "Solfeggio",
        "Colored Noise"
    )

    val outcomeCards = listOf(
        QuickOutcomeCard("Deep Sleep", "0.5 - 4 Hz Delta", Icons.Default.NightsStay, Color(0xFF8B5CF6), "sleep"),
        QuickOutcomeCard("Neuro Focus", "14 - 30 Hz Beta", Icons.Default.Psychology, Color(0xFF06B6D4), "focus"),
        QuickOutcomeCard("Rapid Action", "40 Hz Gamma", Icons.Default.LocalFireDepartment, Color(0xFFF97316), "gamma"),
        QuickOutcomeCard("Cellular Heal", "528 Hz Solfeggio", Icons.Default.Diamond, Color(0xFF10B981), "528"),
        QuickOutcomeCard("Heart Opening", "639 Hz Harmonic", Icons.Default.Favorite, Color(0xFFF43F5E), "639"),
        QuickOutcomeCard("Meditation", "432 Hz Resonance", Icons.Default.Spa, Color(0xFFF59E0B), "meditation")
    )

    val filteredList = (if (query.isNotBlank()) searchResults else allPresets).filter { preset ->
        when (selectedFilter) {
            "0.5-4Hz (Delta)" -> preset.technical_hz.contains("Delta", ignoreCase = true) || preset.category.contains("Sleep", ignoreCase = true)
            "4-8Hz (Theta)" -> preset.technical_hz.contains("Theta", ignoreCase = true) || preset.category.contains("Meditation", ignoreCase = true)
            "8-12Hz (Alpha)" -> preset.technical_hz.contains("Alpha", ignoreCase = true) || preset.category.contains("Calm", ignoreCase = true)
            "12-30Hz (Beta)" -> preset.technical_hz.contains("Beta", ignoreCase = true) || preset.category.contains("Focus", ignoreCase = true)
            "30-100Hz (Gamma)" -> preset.technical_hz.contains("Gamma", ignoreCase = true) || preset.category.contains("Drive", ignoreCase = true)
            "Solfeggio" -> preset.audio_type.contains("Solfeggio", ignoreCase = true)
            "Colored Noise" -> preset.audio_type.contains("Noise", ignoreCase = true)
            else -> true
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AetheriaBackground)
            .statusBarsPadding()
            .testTag("search_screen")
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header: Title & Subtitle + Optional Back button
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
                                    .testTag("search_back_button")
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
                                text = "Explore",
                                color = AetheriaOnSurface,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = (-0.5).sp
                            )
                            Text(
                                text = "Frequency catalog & targeted acoustics",
                                color = AetheriaOnSurfaceVariant,
                                fontSize = 12.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = { /* Open filters toggle */ },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(AetheriaSurfaceContainerHigh)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Filters",
                            tint = AetheriaOnSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Modern Pill Search Input Box
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp)),
                    shape = RoundedCornerShape(18.dp),
                    color = AetheriaSurfaceContainer,
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = AetheriaPrimary,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Box(modifier = Modifier.weight(1f)) {
                            if (query.isEmpty()) {
                                Text(
                                    text = "Search Hz, state, or acoustic outcome...",
                                    color = AetheriaOutline,
                                    fontSize = 14.sp
                                )
                            }
                            BasicTextField(
                                value = query,
                                onValueChange = { viewModel.onSearchQueryChanged(it) },
                                textStyle = TextStyle(
                                    color = AetheriaOnSurface,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal
                                ),
                                cursorBrush = SolidColor(AetheriaPrimary),
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("search_text_field")
                            )
                        }

                        if (query.isNotEmpty()) {
                            IconButton(
                                onClick = { viewModel.onSearchQueryChanged("") },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear",
                                    tint = AetheriaOnSurfaceVariant,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Horizontal Filter Chips Row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filterTabs.forEach { tab ->
                        val isSelected = tab == selectedFilter
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(if (isSelected) AetheriaPrimary else AetheriaSurfaceContainerHigh)
                                .clickable { selectedFilter = tab }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
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

            // Quick Targeted Acoustic Outcomes Section (only when not searching deeply)
            if (query.isEmpty() && selectedFilter == "All Frequencies") {
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Targeted Acoustic Outcomes",
                            color = AetheriaOnSurface,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )

                        // 2x3 Grid of Outcome Cards
                        val chunked = outcomeCards.chunked(2)
                        chunked.forEach { rowItems ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                rowItems.forEach { card ->
                                    Surface(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(84.dp)
                                            .clip(RoundedCornerShape(16.dp))
                                            .clickable { viewModel.onSearchQueryChanged(card.queryTerm) },
                                        shape = RoundedCornerShape(16.dp),
                                        color = AetheriaSurfaceContainerLow
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(38.dp)
                                                    .clip(RoundedCornerShape(10.dp))
                                                    .background(card.color.copy(alpha = 0.16f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = card.icon,
                                                    contentDescription = null,
                                                    tint = card.color,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }

                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = card.title,
                                                    color = AetheriaOnSurface,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    text = card.range,
                                                    color = AetheriaOnSurfaceVariant,
                                                    fontSize = 11.sp,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Results count header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (query.isNotEmpty()) "Search Results" else "Frequency Catalog",
                        color = AetheriaOnSurface,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "${filteredList.size} Tracks",
                        color = AetheriaOutline,
                        fontSize = 12.sp
                    )
                }
            }

            // Presets List
            items(filteredList, key = { it.id }) { preset ->
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
                            viewModel.playPreset(context, preset, filteredList)
                            viewModel.setPlayerExpanded(true)
                        }
                    },
                    onFavoriteToggle = {
                        viewModel.toggleFavorite(preset)
                    }
                )
            }

            // Bottom spacer
            item {
                Spacer(modifier = Modifier.height(140.dp))
            }
        }
    }
}
