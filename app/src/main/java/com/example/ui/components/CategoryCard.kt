package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CategorySummary
import com.example.ui.theme.AetheriaOnSurface
import com.example.ui.theme.AetheriaOnSurfaceVariant
import com.example.ui.theme.AetheriaOutline
import com.example.ui.theme.AetheriaSurfaceContainer
import com.example.ui.theme.AetheriaSurfaceContainerHigh

data class CategoryMeta(
    val icon: ImageVector,
    val color: Color,
    val badge: String,
    val frequencyRange: String
)

@Composable
fun CategoryCard(
    category: CategorySummary,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val meta = getCategoryMeta(category.name)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(178.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .testTag("category_card_${category.name.replace(" ", "_")}"),
        shape = RoundedCornerShape(20.dp),
        color = AetheriaSurfaceContainer,
        shadowElevation = 3.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp)
        ) {
            // Ambient corner glow
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .offset(x = 24.dp, y = (-24).dp)
                    .align(Alignment.TopEnd)
                    .clip(CircleShape)
                    .background(meta.color.copy(alpha = 0.22f))
                    .blur(20.dp)
            )

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Row: Icon Container + State Tag Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(AetheriaSurfaceContainerHigh),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = meta.icon,
                            contentDescription = category.name,
                            tint = meta.color,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(meta.color.copy(alpha = 0.14f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = meta.badge,
                            color = meta.color,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                    }
                }

                // Middle: Title + Scientific Frequency Info
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(
                        text = category.name,
                        color = AetheriaOnSurface,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 19.sp
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = meta.frequencyRange,
                        color = AetheriaOnSurfaceVariant,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Bottom Row: Track count + Arrow circle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${category.presetCount} Tracks",
                        color = AetheriaOutline,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(AetheriaSurfaceContainerHigh),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Open",
                            tint = AetheriaOnSurface,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun getCategoryMeta(categoryName: String): CategoryMeta {
    val lower = categoryName.lowercase()
    return when {
        lower.contains("sleep") || lower.contains("recovery") || lower.contains("rest") -> {
            CategoryMeta(
                icon = Icons.Default.NightsStay,
                color = Color(0xFF8B5CF6),
                badge = "DELTA",
                frequencyRange = "0.5 - 4 Hz Delta"
            )
        }
        lower.contains("work") || lower.contains("study") || lower.contains("cognition") -> {
            CategoryMeta(
                icon = Icons.Default.Psychology,
                color = Color(0xFF06B6D4),
                badge = "BETA",
                frequencyRange = "14 - 30 Hz Beta"
            )
        }
        lower.contains("drive") || lower.contains("motivation") || lower.contains("energy") -> {
            CategoryMeta(
                icon = Icons.Default.LocalFireDepartment,
                color = Color(0xFFF97316),
                badge = "GAMMA",
                frequencyRange = "Gamma Spark 40 Hz"
            )
        }
        lower.contains("wealth") || lower.contains("abundance") || lower.contains("miracle") -> {
            CategoryMeta(
                icon = Icons.Default.Diamond,
                color = Color(0xFF10B981),
                badge = "528 HZ",
                frequencyRange = "528 Hz Miracles"
            )
        }
        lower.contains("love") || lower.contains("attraction") || lower.contains("heart") -> {
            CategoryMeta(
                icon = Icons.Default.Favorite,
                color = Color(0xFFF43F5E),
                badge = "639 HZ",
                frequencyRange = "639 Hz Harmonic"
            )
        }
        lower.contains("chakra") || lower.contains("balance") || lower.contains("meditation") -> {
            CategoryMeta(
                icon = Icons.Default.SelfImprovement,
                color = Color(0xFFF59E0B),
                badge = "432 HZ",
                frequencyRange = "432 Hz Resonance"
            )
        }
        lower.contains("binaural") || lower.contains("focus") -> {
            CategoryMeta(
                icon = Icons.Default.Headphones,
                color = Color(0xFF38BDF8),
                badge = "BINAURAL",
                frequencyRange = "Stereo Separation"
            )
        }
        else -> {
            CategoryMeta(
                icon = Icons.Default.GraphicEq,
                color = Color(0xFFA078FF),
                badge = "SOLFEGGIO",
                frequencyRange = "Pure Harmonic Tone"
            )
        }
    }
}
