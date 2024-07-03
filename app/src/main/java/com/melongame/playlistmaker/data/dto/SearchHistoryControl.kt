package com.melongame.playlistmaker.data.dto

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.melongame.playlistmaker.domain.models.Track
import com.melongame.playlistmaker.presentation.TrackAdapter

class SearchHistoryControl(context: Context) {

    private val sharedPreferences =
        context.getSharedPreferences("SearchHistory", Context.MODE_PRIVATE)
    private val gson = Gson()

    var historyAdapter: TrackAdapter? = null

    fun getSearchHistory(): MutableList<Track> {
        val json = sharedPreferences.getString("search_history", null)
        val type = object : TypeToken<MutableList<Track>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }

    fun addToSearchHistory(track: Track) {
        val currentHistory = getSearchHistory()
        currentHistory.removeAll { it.trackId == track.trackId }
        currentHistory.add(0, track)

        if (currentHistory.size > 10) {
            currentHistory.removeAt(10)
        }

        val json = gson.toJson(currentHistory)
        sharedPreferences.edit().putString("search_history", json).apply()

        historyAdapter?.updateTracks(currentHistory)
    }

    fun clearSearchHistory() {
        sharedPreferences.edit().remove("search_history").apply()
        historyAdapter?.updateTracks(mutableListOf())
    }
}