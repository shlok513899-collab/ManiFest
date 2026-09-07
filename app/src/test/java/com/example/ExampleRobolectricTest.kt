package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.AppDatabase
import com.example.data.FavoritePresetEntity
import com.example.data.PresetRepository
import com.example.model.NoiseColor
import com.example.model.SynthesisMode
import com.example.model.ToneConfig
import com.example.service.PlaybackStateManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Aetheria", appName)
    }

    @Test
    fun `load presets from assets`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = PresetRepository(context)
        val presets = repo.getPresets()

        assertTrue("Presets should not be empty", presets.isNotEmpty())
        val categories = repo.getCategories()
        assertTrue("Categories should not be empty", categories.isNotEmpty())

        val searchResults = repo.searchPresets("Focus")
        assertTrue("Search for 'Focus' should return results", searchResults.isNotEmpty())
    }

    @Test
    fun `tone config parser for all synthesis modes`() {
        // Binaural Beat
        val binaural = ToneConfig.parse("Binaural Beat", "200 Hz base / 40 Hz Gamma")
        assertEquals(SynthesisMode.BINAURAL_BEAT, binaural.mode)
        assertEquals(200f, binaural.baseHz)
        assertEquals(40f, binaural.beatOrPulseHz)
        assertTrue(binaural.isStereoHeadphonesRequired)

        // Pure Solfeggio Tone
        val pure = ToneConfig.parse("Solfeggio Tone", "528 Hz")
        assertEquals(SynthesisMode.PURE_TONE, pure.mode)
        assertEquals(528f, pure.pureToneHz)
        assertFalse(pure.isStereoHeadphonesRequired)

        // Isochronic Pulse
        val isochronic = ToneConfig.parse("Isochronic Pulse", "10 Hz / 200 Hz carrier")
        assertEquals(SynthesisMode.ISOCHRONIC_PULSE, isochronic.mode)
        assertEquals(10f, isochronic.beatOrPulseHz)
        assertEquals(200f, isochronic.carrierHz)

        // Colored Noise
        val noise = ToneConfig.parse("Colored Noise", "Brown Noise")
        assertEquals(SynthesisMode.COLORED_NOISE, noise.mode)
        assertEquals(NoiseColor.BROWN, noise.noiseColor)
        assertTrue(noise.hasNoise)
    }

    @Test
    fun `room database favorites insertion and retrieval`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        val dao = db.favoriteDao()

        val fav = FavoritePresetEntity(
            id = "test_preset",
            title = "Test Tone",
            category = "Sleep",
            audio_type = "Pure tone",
            technical_hz = "432 Hz",
            target_outcome = "Deep relaxation"
        )

        dao.insert(fav)
        val favorites = dao.getAllFavorites().first()
        assertEquals(1, favorites.size)
        assertEquals("Test Tone", favorites[0].title)

        val isFav = dao.isFavorite("test_preset").first()
        assertTrue(isFav)

        dao.deleteById("test_preset")
        val isFavAfterDelete = dao.isFavorite("test_preset").first()
        assertFalse(isFavAfterDelete)

        db.close()
    }

    @Test
    fun `audio playback toggle pause and state transitions`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = PresetRepository(context)
        val preset = repo.getPresets().first()

        // Play preset
        PlaybackStateManager.playPreset(context, preset)
        assertTrue("Playback should be playing after playPreset", PlaybackStateManager.isPlaying.value)

        // Pause playback
        PlaybackStateManager.pause()
        assertFalse("Playback should be paused immediately after pause()", PlaybackStateManager.isPlaying.value)

        // Toggle playback to resume
        PlaybackStateManager.togglePlayPause(context)
        assertTrue("Playback should be playing after togglePlayPause", PlaybackStateManager.isPlaying.value)

        // Toggle playback to pause
        PlaybackStateManager.togglePlayPause(context)
        assertFalse("Playback should be paused after togglePlayPause", PlaybackStateManager.isPlaying.value)

        // Stop cleanly
        PlaybackStateManager.stop(context)
        assertFalse("Playback should not be playing after stop", PlaybackStateManager.isPlaying.value)
    }
}
