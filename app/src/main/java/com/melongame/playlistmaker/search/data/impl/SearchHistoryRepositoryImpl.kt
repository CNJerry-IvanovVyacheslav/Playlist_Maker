package com.melongame.playlistmaker.search.data.impl

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.melongame.playlistmaker.search.domain.api.SearchHistoryRepository
import com.melongame.playlistmaker.search.domain.models.Track

class SearchHistoryRepositoryImpl(private val sharedPref: SharedPreferences) :
    SearchHistoryRepository {

    private val gson = Gson()
    private val editor = sharedPref.edit()


    override fun saveTrack(track: Track) {
        val history = getSearchHistory().toMutableList()

        if (history.contains(track)) {
            history.remove(track)
            Log.d("HistoryChanger", "Удален имеющийся трек!")
        }
        history.add(TRACK_INDEX_FIRST, track)
        if (history.size > TRACK_INDEX_MAX) {
            history.removeLast()
            Log.d("HistoryChanger", "Удален лишний трек!")
        }
        val json = gson.toJson(history)
        editor.putString(SEARCH_HISTORY_KEY, json)
        editor.apply()
        Log.d("HistoryChanger", "Трек сохранен!")
    }


    override fun getSearchHistory(): List<Track> {
        val json = sharedPref.getString(SEARCH_HISTORY_KEY, null)
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        Log.d("HistoryChanger", "История поиска получена!")
        return gson.fromJson(json, type) ?: emptyList()
    }


    override fun clearSearchHistory() {
        editor.remove(SEARCH_HISTORY_KEY)
        editor.apply()
        Log.d("HistoryChanger", "История поиска отчищена!")
    }


    private companion object {
        const val TRACK_INDEX_MAX = 10
        const val TRACK_INDEX_FIRST = 0
        const val SEARCH_HISTORY_KEY = "search_history"
    }
}