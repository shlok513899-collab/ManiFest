package com.example

import android.app.Application
import com.example.data.AppDatabase
import com.example.data.FavoritesRepository
import com.example.data.PresetRepository

class ResonanceApplication : Application() {

    lateinit var presetRepository: PresetRepository
        private set

    lateinit var favoritesRepository: FavoritesRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val database = AppDatabase.getDatabase(this)
        favoritesRepository = FavoritesRepository(database.favoriteDao())
        presetRepository = PresetRepository(this)
    }
}
