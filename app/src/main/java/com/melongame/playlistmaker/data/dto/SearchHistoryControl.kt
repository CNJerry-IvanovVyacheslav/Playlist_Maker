package com.melongame.playlistmaker.data.dto

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.melongame.playlistmaker.domain.models.Track

class SearchHistoryControl(context: Context) {

    private val sharedPreferences =
        context.getSharedPreferences("SearchHistory", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun getSearchHistory(): MutableList<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY_STRING, null)
        val type = object : TypeToken<MutableList<Track>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }

    fun addToSearchHistory(track: Track): MutableList<Track> {
        val currentHistory = getSearchHistory()
        currentHistory.removeAll { it.trackId == track.trackId }
        currentHistory.add(TRACK_INDEX_FIRST, track)
        if (currentHistory.size > TRACK_INDEX_LAST) {
            currentHistory.removeAt(TRACK_INDEX_LAST)
        }
        val json = gson.toJson(currentHistory)
        sharedPreferences.edit().putString(SEARCH_HISTORY_STRING, json).apply()

        return currentHistory
    }

    fun clearSearchHistory() {
        sharedPreferences.edit().remove(SEARCH_HISTORY_STRING).apply()
    }

    private companion object {
        const val SEARCH_HISTORY_STRING = "search_history"
        const val TRACK_INDEX_FIRST = 0
        const val TRACK_INDEX_LAST = 10
    }
}