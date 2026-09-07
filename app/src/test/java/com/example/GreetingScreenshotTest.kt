package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.core.app.ApplicationProvider
import android.content.Context
import androidx.room.Room
import com.example.data.AppDatabase
import com.example.data.FavoritesRepository
import com.example.data.PresetRepository
import com.example.ui.screens.HomeScreen
import com.example.ui.theme.ResonanceTheme
import com.example.ui.viewmodel.FrequencyPlayerViewModel
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [34])
class GreetingScreenshotTest {

    @get:Rule val composeTestRule = createComposeRule()

    @Test
    fun app_home_screenshot() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        val favoritesRepo = FavoritesRepository(db.favoriteDao())
        val presetRepo = PresetRepository(context)
        val viewModel = FrequencyPlayerViewModel(presetRepo, favoritesRepo)

        composeTestRule.setContent {
            ResonanceTheme {
                HomeScreen(
                    viewModel = viewModel,
                    onCategoryClick = {},
                    onSearchClick = {},
                    onFavoritesClick = {}
                )
            }
        }

        composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/home.png")
        db.close()
    }
}
