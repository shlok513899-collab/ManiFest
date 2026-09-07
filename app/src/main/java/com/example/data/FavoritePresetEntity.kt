package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoritePresetEntity(
    @PrimaryKey val id: String,
    val title: String,
    val category: String,
    val audio_type: String,
    val technical_hz: String,
    val target_outcome: String,
    val timestamp: Long = System.currentTimeMillis()
)
