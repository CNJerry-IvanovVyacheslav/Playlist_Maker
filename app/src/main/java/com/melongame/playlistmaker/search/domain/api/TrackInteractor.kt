package com.melongame.playlistmaker.search.domain.api

import com.melongame.playlistmaker.search.domain.models.Track
import com.melongame.playlistmaker.util.SearchResult

interface TrackInteractor {
    fun searchTrack(expression: String, consumer: (SearchResult<List<Track>>) -> Unit)
    fun saveTrack(track: Track)
    fun getSearchHistory(): List<Track>
    fun clearSearchHistory()
}