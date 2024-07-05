package com.melongame.playlistmaker.data.dto

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.melongame.playlistmaker.data.TrackAdapterRepositoryImpl
import com.melongame.playlistmaker.domain.models.Track

class SearchHistoryControl(context: Context) {

    var trackAdapterRepository = TrackAdapterRepositoryImpl()

    private val sharedPreferences =
        context.getSharedPreferences("SearchHistory", Context.MODE_PRIVATE)
    private val gson = Gson()
    fun getSearchHistory(): MutableList<Track> {
        val json = sharedPreferences.getString("search_history", null)
        val type = object : TypeToken<MutableList<Track>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }
    fun addToSearchHistory(track: Track): MutableList<Track> {
        val currentHistory = getSearchHistory()
        currentHistory.removeAll { it.trackId == track.trackId }
        currentHistory.add(0, track)
        if (currentHistory.size > 10) {
            currentHistory.removeAt(10)
        }
        val json = gson.toJson(currentHistory)
        sharedPreferences.edit().putString("search_history", json).apply()

        return currentHistory
    }
    fun clearSearchHistory() {
        sharedPreferences.edit().remove("search_history").apply()
    }
}