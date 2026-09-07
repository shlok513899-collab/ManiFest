package com.example.data

import com.example.model.FrequencyPreset
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepository(private val favoriteDao: FavoriteDao) {

    val allFavorites: Flow<List<FavoritePresetEntity>> = favoriteDao.getAllFavorites()

    val favoriteIds: Flow<Set<String>> = allFavorites.map { list ->
        list.map { it.id }.toSet()
    }

    suspend fun toggleFavorite(preset: FrequencyPreset, isCurrentlyFavorite: Boolean) {
        if (isCurrentlyFavorite) {
            favoriteDao.deleteById(preset.id)
        } else {
            favoriteDao.insert(
                FavoritePresetEntity(
                    id = preset.id,
                    title = preset.title,
                    category = preset.category,
                    audio_type = preset.audio_type,
                    technical_hz = preset.technical_hz,
                    target_outcome = preset.target_outcome
                )
            )
        }
    }

    fun isFavorite(id: String): Flow<Boolean> = favoriteDao.isFavorite(id)
}
