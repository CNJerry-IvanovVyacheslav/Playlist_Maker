package com.melongame.playlistmaker.search.domain.api

import com.melongame.playlistmaker.search.domain.models.Track

interface SearchHistoryRepository {
    fun saveTrack(track: Track)
    fun getSearchHistory(): List<Track>
    fun clearSearchHistory()
}