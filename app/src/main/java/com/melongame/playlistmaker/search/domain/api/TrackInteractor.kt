package com.melongame.playlistmaker.search.domain.api

import com.melongame.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {
    fun searchTrack(expression: String): Flow<Pair<List<Track>?, Int?>>
    fun saveTrack(track: Track)
    fun getSearchHistory(): List<Track>
    fun clearSearchHistory()
}