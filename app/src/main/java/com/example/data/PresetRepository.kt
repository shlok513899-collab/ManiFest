package com.example.data

import android.content.Context
import com.example.model.FrequencyPreset
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStreamReader

data class CategorySummary(
    val name: String,
    val presetCount: Int,
    val description: String = "",
    val samplePreset: FrequencyPreset? = null
)

class PresetRepository(private val context: Context) {

    private var cachedPresets: List<FrequencyPreset>? = null

    fun getPresets(): List<FrequencyPreset> {
        cachedPresets?.let { return it }

        val list = mutableListOf<FrequencyPreset>()
        try {
            val jsonString = context.assets.open("presets.json").use { stream ->
                InputStreamReader(stream, Charsets.UTF_8).readText()
            }

            val trimmed = jsonString.trim()
            if (trimmed.startsWith("[")) {
                val array = JSONArray(trimmed)
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    list.add(parsePresetObject(obj))
                }
            } else if (trimmed.startsWith("{")) {
                val root = JSONObject(trimmed)
                if (root.has("categories")) {
                    val catArray = root.getJSONArray("categories")
                    for (i in 0 until catArray.length()) {
                        val catObj = catArray.getJSONObject(i)
                        val catName = catObj.optString("category", "Wellness")
                        if (catObj.has("presets")) {
                            val presetArr = catObj.getJSONArray("presets")
                            for (j in 0 until presetArr.length()) {
                                val pObj = presetArr.getJSONObject(j)
                                list.add(parsePresetObject(pObj, catName))
                            }
                        }
                    }
                } else if (root.has("presets")) {
                    val presetArr = root.getJSONArray("presets")
                    for (i in 0 until presetArr.length()) {
                        val pObj = presetArr.getJSONObject(i)
                        list.add(parsePresetObject(pObj))
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        cachedPresets = list
        return list
    }

    private fun parsePresetObject(obj: JSONObject, fallbackCategory: String = "Wellness"): FrequencyPreset {
        val category = obj.optString("category", fallbackCategory)
        val title = obj.optString("title", "Harmonic Frequency")
        val audioType = obj.optString("audio_type", "Pure tone")
        val technicalHz = obj.optString("technical_hz", "432 Hz")
        val targetOutcome = obj.optString("target_outcome", "Peace and harmony")

        val tagsList = mutableListOf<String>()
        if (obj.has("tags")) {
            val tagsArr = obj.optJSONArray("tags")
            if (tagsArr != null) {
                for (k in 0 until tagsArr.length()) {
                    tagsList.add(tagsArr.getString(k))
                }
            }
        }

        return FrequencyPreset(
            category = category,
            title = title,
            audio_type = audioType,
            technical_hz = technicalHz,
            target_outcome = targetOutcome,
            tags = tagsList
        )
    }

    fun getCategories(): List<CategorySummary> {
        val presets = getPresets()
        val grouped = presets.groupBy { it.category }
        return grouped.map { (catName, catPresets) ->
            CategorySummary(
                name = catName,
                presetCount = catPresets.size,
                samplePreset = catPresets.firstOrNull()
            )
        }.sortedByDescending { it.presetCount }
    }

    fun getPresetsByCategory(category: String): List<FrequencyPreset> {
        return getPresets().filter { it.category.equals(category, ignoreCase = true) }
    }

    fun searchPresets(query: String): List<FrequencyPreset> {
        if (query.isBlank()) return emptyList()
        val q = query.trim().lowercase()
        return getPresets().filter { preset ->
            preset.title.lowercase().contains(q) ||
            preset.category.lowercase().contains(q) ||
            preset.target_outcome.lowercase().contains(q) ||
            preset.audio_type.lowercase().contains(q) ||
            preset.technical_hz.lowercase().contains(q) ||
            preset.tags.any { it.lowercase().contains(q) }
        }
    }
}
