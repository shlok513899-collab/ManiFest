package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FrequencyPreset
import com.example.ui.components.CategoryCard
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
import com.example.ui.theme.AetheriaTertiary
import com.example.ui.viewmodel.FrequencyPlayerViewModel
import java.util.Calendar

data class QuickHistoryItem(
    val title: String,
    val subtitle: String,
    val tag: String,
    val duration: String,
    val icon: ImageVector,
    val accentColor: Color
)

@Composable
fun HomeScreen(
    viewModel: FrequencyPlayerViewModel,
    onCategoryClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val categories by viewModel.categories.collectAsState()
    val presets by viewModel.presets.collectAsState()

    // Determine time-appropriate greeting
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val greeting = when {
        hour < 12 -> "Good morning, Alex"
        hour < 17 -> "Good afternoon, Alex"
        else -> "Good evening, Alex"
    }
    val optimalState = when {
        hour < 12 -> "Deep Focus & Mental Clarity"
        hour < 17 -> "Drive & Creative Flow"
        else -> "Sleep & Deep Reset"
    }

    // Pinging green dot animation
    val infiniteTransition = rememberInfiniteTransition(label = "PingAnimation")
    val pingScale by infiniteTransition.animateFloat(
        initialValue = 0.8f, targetValue = 1.3f,
        animationSpec = infiniteRepeatable(tween(1200, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "ping"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AetheriaBackground)
            .testTag("home_screen")
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Section: Bio-Acoustic Greeting + Action Buttons
            item(span = { GridItemSpan(2) }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(AetheriaTertiary)
                            )
                            Text(
                                text = "BIO-ACOUSTIC SYNC ACTIVE",
                                color = AetheriaTertiary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = greeting,
                            color = AetheriaOnSurface,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.5).sp
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bedtime,
                                contentDescription = null,
                                tint = AetheriaPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "Optimal for: ",
                                color = AetheriaOnSurfaceVariant,
                                fontSize = 12.sp
                            )
                            Text(
                                text = optimalState,
                                color = AetheriaPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Search & Notification action buttons
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = onSearchClick,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(AetheriaSurfaceContainerHigh)
                                .testTag("search_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = AetheriaOnSurfaceVariant,
                                modifier = Modifier.size(19.dp)
                            )
                        }

                        Box {
                            IconButton(
                                onClick = { viewModel.setShowOnboardingDialog(true) },
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(AetheriaSurfaceContainerHigh)
                                    .testTag("notifications_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notifications",
                                    tint = AetheriaOnSurfaceVariant,
                                    modifier = Modifier.size(19.dp)
                                )
                            }
                            // Cyan badge dot
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .align(Alignment.TopEnd)
                                    .padding(top = 8.dp, end = 8.dp)
                                    .clip(CircleShape)
                                    .background(AetheriaSecondary)
                            )
                        }
                    }
                }
            }

            // Featured Circadian Entrainment Banner Card
            item(span = { GridItemSpan(2) }) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .testTag("circadian_banner"),
                    shape = RoundedCornerShape(20.dp),
                    color = AetheriaSurfaceContainer,
                    shadowElevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "CIRCADIAN ENTRAINMENT",
                                    color = AetheriaSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "Evening Restorative Field",
                                    color = AetheriaOnSurface,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "Atmospheric delta pulses paired with 432Hz ambient rain to reduce neural velocity.",
                                    color = AetheriaOnSurfaceVariant,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            // Glowing circular play button
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(AetheriaPrimary)
                                    .clickable {
                                        // Find a delta / restorative preset to play
                                        val target = presets.firstOrNull { it.title.contains("Delta", ignoreCase = true) }
                                            ?: presets.firstOrNull()
                                        target?.let {
                                            viewModel.playPreset(context, it, presets)
                                            viewModel.setPlayerExpanded(true)
                                        }
                                    }
                                    .testTag("circadian_play_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Play Circadian Field",
                                    tint = AetheriaOnPrimary,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Footnote Chips
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(AetheriaSurfaceContainerHighest)
                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.GraphicEq,
                                    contentDescription = null,
                                    tint = AetheriaPrimary,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = "0.5 - 2.5 Hz Delta",
                                    color = AetheriaOnSurface,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(AetheriaSurfaceContainerHighest)
                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = null,
                                    tint = AetheriaSecondary,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = "45 min session",
                                    color = AetheriaOnSurface,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // "Jump Back In" Section
            item(span = { GridItemSpan(2) }) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                tint = AetheriaPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Jump Back In",
                                color = AetheriaOnSurface,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Text(
                            text = "See all (${presets.take(12).size})",
                            color = AetheriaPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable { onSearchClick() }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Horizontal scrolling carousel
                    val quickHistory = listOf(
                        QuickHistoryItem("Deep Lucid Slumber", "2.2 Hz Isochronic", "Delta", "Last: 32 min", Icons.Default.DarkMode, AetheriaPrimary),
                        QuickHistoryItem("Hyper-Focus Flow", "18 Hz Binaural", "Beta", "Last: 60 min", Icons.Default.Psychology, AetheriaSecondary),
                        QuickHistoryItem("Cellular Miracles", "528 Hz Pure Tone", "Solfeggio", "Last: 20 min", Icons.Default.AutoAwesome, AetheriaTertiary),
                        QuickHistoryItem("Crown Alignment", "432 Hz Harmonic", "Om", "Last: 15 min", Icons.Default.Spa, AetheriaPrimary)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        quickHistory.forEach { item ->
                            Surface(
                                modifier = Modifier
                                    .width(168.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .clickable {
                                        val match = presets.firstOrNull { it.title.contains(item.title.split(" ").first(), ignoreCase = true) }
                                            ?: presets.firstOrNull()
                                        match?.let {
                                            viewModel.playPreset(context, it, presets)
                                            viewModel.setPlayerExpanded(true)
                                        }
                                    },
                                shape = RoundedCornerShape(18.dp),
                                color = AetheriaSurfaceContainer
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(34.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(AetheriaSurfaceContainerHigh),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = item.icon,
                                                contentDescription = null,
                                                tint = item.accentColor,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }

                                        Box(
                                            modifier = Modifier
                                                .clip(CircleShape)
                                                .background(AetheriaSurfaceContainerHighest)
                                                .padding(horizontal = 7.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = item.tag,
                                                color = item.accentColor,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Text(
                                        text = item.title,
                                        color = AetheriaOnSurface,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Text(
                                        text = item.subtitle,
                                        color = AetheriaOnSurfaceVariant,
                                        fontSize = 11.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = item.duration,
                                            color = AetheriaOutline,
                                            fontSize = 10.sp
                                        )

                                        Icon(
                                            imageVector = Icons.Default.PlayCircle,
                                            contentDescription = "Play",
                                            tint = item.accentColor,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // "Explore Frequencies" Section Header
            item(span = { GridItemSpan(2) }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 4.dp)
                ) {
                    Text(
                        text = "Explore Frequencies",
                        color = AetheriaOnSurface,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Attune your consciousness to scientific resonance states",
                        color = AetheriaOnSurfaceVariant,
                        fontSize = 13.sp
                    )
                }
            }

            // Category Cards Grid
            items(categories, key = { it.name }) { category ->
                CategoryCard(
                    category = category,
                    onClick = { onCategoryClick(category.name) }
                )
            }

            // Bottom spacer for Mini Player and Nav Bar
            item(span = { GridItemSpan(2) }) {
                Spacer(modifier = Modifier.height(140.dp))
            }
        }
    }
}
