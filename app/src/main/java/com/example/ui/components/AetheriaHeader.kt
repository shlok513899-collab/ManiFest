package com.example.ui.components

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.AetheriaBackground
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

@Composable
fun AetheriaHeader(
    screenTitle: String,
    modifier: Modifier = Modifier,
    onOpenProfile: (() -> Unit)? = null
) {
    var showProfileModal by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AetheriaBackground.copy(alpha = 0.95f))
            .statusBarsPadding()
            .testTag("aetheria_header")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Brand + Screen Breadcrumb
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_aetheria_logo),
                    contentDescription = "Aetheria Logo",
                    modifier = Modifier.size(30.dp)
                )

                Text(
                    text = "Aetheria",
                    color = AetheriaOnSurface,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.5.sp
                )

                Text(
                    text = "/",
                    color = AetheriaOutline.copy(alpha = 0.4f),
                    fontSize = 16.sp
                )

                Text(
                    text = screenTitle,
                    color = AetheriaOnSurfaceVariant,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            }

            // Profile Avatar Pill Button
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(AetheriaPrimary)
                    .clickable {
                        if (onOpenProfile != null) {
                            onOpenProfile()
                        } else {
                            showProfileModal = true
                        }
                    }
                    .testTag("header_profile_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User Profile",
                    tint = AetheriaOnPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }

    if (showProfileModal) {
        AlertDialog(
            onDismissRequest = { showProfileModal = false },
            containerColor = AetheriaSurfaceContainer,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(AetheriaPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = AetheriaOnPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Alex Mercer",
                            color = AetheriaOnSurface,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Resonance Calibrated",
                            color = AetheriaTertiary,
                            fontSize = 12.sp
                        )
                    }
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CircleShape)
                            .background(AetheriaSurfaceContainerLow)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = AetheriaSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Real-time on-device audio synthesis active",
                            color = AetheriaOnSurfaceVariant,
                            fontSize = 12.sp
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CircleShape)
                            .background(AetheriaSurfaceContainerLow)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = AetheriaPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "34 Therapeutic Frequencies Available",
                            color = AetheriaOnSurfaceVariant,
                            fontSize = 12.sp
                        )
                    }

                    Text(
                        text = "Aetheria synthesizes continuous, infinite mathematical frequencies locally without network streaming, preserving device battery and audio fidelity.",
                        color = AetheriaOutline,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showProfileModal = false },
                    colors = ButtonDefaults.buttonColors(containerColor = AetheriaPrimary)
                ) {
                    Text("Close", color = AetheriaOnPrimary)
                }
            }
        )
    }
}
