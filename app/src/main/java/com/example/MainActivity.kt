package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AetheriaBottomNav
import com.example.ui.components.AetheriaHeader
import com.example.ui.components.HeadphoneNoticeDialog
import com.example.ui.components.MiniPlayer
import com.example.ui.components.OnboardingDialog
import com.example.ui.components.SleepTimerDialog
import com.example.ui.screens.CategoryDetailScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PlayerScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.theme.AetheriaBackground
import com.example.ui.theme.ResonanceTheme
import com.example.ui.viewmodel.FrequencyPlayerViewModel

enum class AppScreen {
    HOME,
    CATEGORY_DETAIL,
    SEARCH,
    FAVORITES,
    PROFILE
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as ResonanceApplication

        setContent {
            ResonanceTheme {
                val viewModel: FrequencyPlayerViewModel = viewModel(
                    factory = FrequencyPlayerViewModel.provideFactory(
                        app.presetRepository,
                        app.favoritesRepository
                    )
                )

                ResonanceApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun ResonanceApp(viewModel: FrequencyPlayerViewModel) {
    val context = LocalContext.current
    var currentScreen by remember { mutableStateOf(AppScreen.HOME) }
    var activeCategoryName by remember { mutableStateOf("") }

    val currentPreset by viewModel.currentPreset.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val isPlayerExpanded by viewModel.isPlayerExpanded.collectAsState()
    val showSleepTimerDialog by viewModel.showSleepTimerDialog.collectAsState()
    val sleepTimerOption by viewModel.sleepTimer.collectAsState()
    val sleepSecondsRemaining by viewModel.sleepSecondsRemaining.collectAsState()
    val showHeadphoneDialog by viewModel.showHeadphoneDialog.collectAsState()
    val showOnboardingDialog by viewModel.showOnboardingDialog.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()

    // Request notification permission for Android 13+
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { /* Permission granted or denied */ }

    LaunchedEffect(Unit) {
        viewModel.checkFirstLaunchOnboarding(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    // System Back Press Handler
    BackHandler(enabled = isPlayerExpanded || currentScreen != AppScreen.HOME) {
        if (isPlayerExpanded) {
            viewModel.setPlayerExpanded(false)
        } else if (currentScreen != AppScreen.HOME) {
            currentScreen = AppScreen.HOME
        }
    }

    val headerTitle = when (currentScreen) {
        AppScreen.HOME -> "Home"
        AppScreen.CATEGORY_DETAIL -> activeCategoryName
        AppScreen.SEARCH -> "Frequency Explorer"
        AppScreen.FAVORITES -> "Saved Frequencies"
        AppScreen.PROFILE -> "Bio-Acoustic Profile"
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(AetheriaBackground),
        containerColor = AetheriaBackground,
        topBar = {
            if (!isPlayerExpanded) {
                AetheriaHeader(
                    screenTitle = headerTitle,
                    onOpenProfile = { currentScreen = AppScreen.PROFILE }
                )
            }
        },
        bottomBar = {
            if (!isPlayerExpanded) {
                AetheriaBottomNav(
                    currentScreen = currentScreen,
                    onSelectScreen = { screen ->
                        currentScreen = screen
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Main Content Area
            when (currentScreen) {
                AppScreen.HOME -> {
                    HomeScreen(
                        viewModel = viewModel,
                        onCategoryClick = { categoryName ->
                            activeCategoryName = categoryName
                            viewModel.selectCategory(categoryName)
                            currentScreen = AppScreen.CATEGORY_DETAIL
                        },
                        onSearchClick = { currentScreen = AppScreen.SEARCH },
                        onFavoritesClick = { currentScreen = AppScreen.FAVORITES }
                    )
                }
                AppScreen.CATEGORY_DETAIL -> {
                    CategoryDetailScreen(
                        categoryName = activeCategoryName,
                        viewModel = viewModel,
                        onBack = { currentScreen = AppScreen.HOME }
                    )
                }
                AppScreen.SEARCH -> {
                    SearchScreen(
                        viewModel = viewModel,
                        onBack = { currentScreen = AppScreen.HOME }
                    )
                }
                AppScreen.FAVORITES -> {
                    FavoritesScreen(
                        viewModel = viewModel,
                        onBack = { currentScreen = AppScreen.HOME }
                    )
                }
                AppScreen.PROFILE -> {
                    ProfileScreen(
                        viewModel = viewModel
                    )
                }
            }

            // Floating Mini-Player bar above bottom navigation bar
            AnimatedVisibility(
                visible = currentPreset != null && !isPlayerExpanded,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 6.dp)
            ) {
                currentPreset?.let { preset ->
                    val isFav = favoriteIds.contains(preset.id)
                    MiniPlayer(
                        preset = preset,
                        isPlaying = isPlaying,
                        onPlayPauseClick = { viewModel.togglePlayPause(context) },
                        onExpandClick = { viewModel.setPlayerExpanded(true) },
                        isFavorite = isFav,
                        onFavoriteToggle = { viewModel.toggleFavorite(preset) }
                    )
                }
            }

            // Full-screen Player overlay
            AnimatedVisibility(
                visible = isPlayerExpanded,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                modifier = Modifier.fillMaxSize()
            ) {
                PlayerScreen(
                    viewModel = viewModel,
                    onCollapse = { viewModel.setPlayerExpanded(false) }
                )
            }
        }
    }

    // Sleep Timer Dialog
    if (showSleepTimerDialog) {
        SleepTimerDialog(
            currentOption = sleepTimerOption,
            remainingSeconds = sleepSecondsRemaining,
            onSelectDuration = { duration ->
                viewModel.setSleepTimer(duration, context)
            },
            onDismiss = { viewModel.setShowSleepTimerDialog(false) }
        )
    }

    // Headphones Explanation Dialog
    if (showHeadphoneDialog) {
        HeadphoneNoticeDialog(
            onDismiss = { viewModel.dismissHeadphoneDialog() }
        )
    }

    // Onboarding Walkthrough Dialog
    if (showOnboardingDialog) {
        OnboardingDialog(
            onDismiss = { viewModel.completeOnboarding(context) }
        )
    }
}
