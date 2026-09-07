package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.AppScreen
import com.example.ui.theme.AetheriaBackground
import com.example.ui.theme.AetheriaOnSurfaceVariant
import com.example.ui.theme.AetheriaPrimary
import com.example.ui.theme.AetheriaSurfaceContainer

data class NavItem(
    val screen: AppScreen,
    val label: String,
    val icon: ImageVector,
    val tag: String
)

@Composable
fun AetheriaBottomNav(
    currentScreen: AppScreen,
    onSelectScreen: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val navItems = listOf(
        NavItem(AppScreen.HOME, "Home", Icons.Default.GraphicEq, "nav_home"),
        NavItem(AppScreen.SEARCH, "Search", Icons.Default.Explore, "nav_search"),
        NavItem(AppScreen.FAVORITES, "Favorites", Icons.Default.Bookmark, "nav_favorites"),
        NavItem(AppScreen.PROFILE, "Profile", Icons.Default.Tune, "nav_profile")
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(AetheriaBackground.copy(alpha = 0.95f))
            .navigationBarsPadding()
            .testTag("aetheria_bottom_nav")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navItems.forEach { item ->
                val isSelected = currentScreen == item.screen

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .clip(CircleShape)
                        .clickable(
                            onClick = { onSelectScreen(item.screen) }
                        )
                        .testTag(item.tag),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (isSelected) AetheriaPrimary else AetheriaOnSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )

                    Text(
                        text = item.label,
                        color = if (isSelected) AetheriaPrimary else AetheriaOnSurfaceVariant,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}
