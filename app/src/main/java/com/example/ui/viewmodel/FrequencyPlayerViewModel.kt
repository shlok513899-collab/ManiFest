package com.example.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.CategorySummary
import com.example.data.FavoritePresetEntity
import com.example.data.FavoritesRepository
import com.example.data.PresetRepository
import com.example.model.FrequencyPreset
import com.example.model.SynthesisMode
import com.example.service.PlaybackStateManager
import com.example.service.SleepTimerDuration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FrequencyPlayerViewModel(
    private val presetRepository: PresetRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    // Presets & Categories
    private val _presets = MutableStateFlow<List<FrequencyPreset>>(emptyList())
    val presets: StateFlow<List<FrequencyPreset>> = _presets.asStateFlow()

    private val _categories = MutableStateFlow<List<CategorySummary>>(emptyList())
    val categories: StateFlow<List<CategorySummary>> = _categories.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _categoryPresets = MutableStateFlow<List<FrequencyPreset>>(emptyList())
    val categoryPresets: StateFlow<List<FrequencyPreset>> = _categoryPresets.asStateFlow()

    // Search
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<FrequencyPreset>>(emptyList())
    val searchResults: StateFlow<List<FrequencyPreset>> = _searchResults.asStateFlow()

    // Favorites
    val favoriteIds: StateFlow<Set<String>> = favoritesRepository.favoriteIds.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptySet()
    )

    val allFavorites: StateFlow<List<FavoritePresetEntity>> = favoritesRepository.allFavorites.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Playback state from manager
    val currentPreset: StateFlow<FrequencyPreset?> = PlaybackStateManager.currentPreset
    val isPlaying: StateFlow<Boolean> = PlaybackStateManager.isPlaying
    val amplitude: StateFlow<Float> = PlaybackStateManager.amplitude
    val masterVolume: StateFlow<Float> = PlaybackStateManager.masterVolume
    val toneVolume: StateFlow<Float> = PlaybackStateManager.toneVolume
    val noiseVolume: StateFlow<Float> = PlaybackStateManager.noiseVolume
    val isLooping: StateFlow<Boolean> = PlaybackStateManager.isLooping
    val sleepTimer: StateFlow<SleepTimerDuration> = PlaybackStateManager.sleepTimer
    val sleepSecondsRemaining: StateFlow<Int?> = PlaybackStateManager.sleepSecondsRemaining

    // Full player expanded state
    private val _isPlayerExpanded = MutableStateFlow(false)
    val isPlayerExpanded: StateFlow<Boolean> = _isPlayerExpanded.asStateFlow()

    // Headphones guidance dialog
    private val _showHeadphoneDialog = MutableStateFlow(false)
    val showHeadphoneDialog: StateFlow<Boolean> = _showHeadphoneDialog.asStateFlow()

    // Sleep timer dialog
    private val _showSleepTimerDialog = MutableStateFlow(false)
    val showSleepTimerDialog: StateFlow<Boolean> = _showSleepTimerDialog.asStateFlow()

    // Onboarding dialog (first launch or manual info)
    private val _showOnboardingDialog = MutableStateFlow(false)
    val showOnboardingDialog: StateFlow<Boolean> = _showOnboardingDialog.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val loadedPresets = presetRepository.getPresets()
            _presets.value = loadedPresets
            _categories.value = presetRepository.getCategories()
        }
    }

    fun selectCategory(categoryName: String) {
        _selectedCategory.value = categoryName
        _categoryPresets.value = presetRepository.getPresetsByCategory(categoryName)
    }

    fun clearCategory() {
        _selectedCategory.value = null
        _categoryPresets.value = emptyList()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        _searchResults.value = if (query.isBlank()) {
            emptyList()
        } else {
            presetRepository.searchPresets(query)
        }
    }

    fun playPreset(context: Context, preset: FrequencyPreset, playlist: List<FrequencyPreset> = emptyList()) {
        val effectivePlaylist = if (playlist.isNotEmpty()) playlist else _presets.value
        PlaybackStateManager.playPreset(context, preset, effectivePlaylist)

        // Check if headphones should be recommended
        if (preset.toneConfig.isStereoHeadphonesRequired) {
            checkAndShowHeadphonesDialog(context)
        }
    }

    fun togglePlayPause(context: Context) {
        PlaybackStateManager.togglePlayPause(context)
    }

    fun playNext(context: Context) {
        PlaybackStateManager.playNext(context)
    }

    fun playPrevious(context: Context) {
        PlaybackStateManager.playPrevious(context)
    }

    fun setMasterVolume(vol: Float) {
        PlaybackStateManager.setMasterVolume(vol)
    }

    fun setToneVolume(vol: Float) {
        PlaybackStateManager.setToneVolume(vol)
    }

    fun setNoiseVolume(vol: Float) {
        PlaybackStateManager.setNoiseVolume(vol)
    }

    fun toggleLoop() {
        PlaybackStateManager.toggleLoop()
    }

    fun setSleepTimer(duration: SleepTimerDuration, context: Context) {
        PlaybackStateManager.setSleepTimer(duration, context)
        _showSleepTimerDialog.value = false
    }

    fun cancelSleepTimer() {
        PlaybackStateManager.cancelSleepTimer()
        _showSleepTimerDialog.value = false
    }

    fun toggleFavorite(preset: FrequencyPreset) {
        viewModelScope.launch {
            val isFav = favoriteIds.value.contains(preset.id)
            favoritesRepository.toggleFavorite(preset, isFav)
        }
    }

    fun setPlayerExpanded(expanded: Boolean) {
        _isPlayerExpanded.value = expanded
    }

    fun setShowSleepTimerDialog(show: Boolean) {
        _showSleepTimerDialog.value = show
    }

    fun setShowOnboardingDialog(show: Boolean) {
        _showOnboardingDialog.value = show
    }

    fun dismissHeadphoneDialog() {
        _showHeadphoneDialog.value = false
    }

    private fun checkAndShowHeadphonesDialog(context: Context) {
        val prefs = context.getSharedPreferences("resonance_prefs", Context.MODE_PRIVATE)
        val hasSeenDialog = prefs.getBoolean("has_seen_headphones_notice", false)
        if (!hasSeenDialog) {
            _showHeadphoneDialog.value = true
            prefs.edit().putBoolean("has_seen_headphones_notice", true).apply()
        }
    }

    fun triggerHeadphonesInfo() {
        _showHeadphoneDialog.value = true
    }

    companion object {
        fun provideFactory(
            presetRepository: PresetRepository,
            favoritesRepository: FavoritesRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FrequencyPlayerViewModel(presetRepository, favoritesRepository) as T
            }
        }
    }
}
