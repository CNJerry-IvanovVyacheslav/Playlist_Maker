package com.melongame.playlistmaker.search.domain.api

import com.melongame.playlistmaker.search.domain.models.Track
import com.melongame.playlistmaker.util.SearchResult
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun searchTrack(expression: String): Flow<SearchResult<List<Track>>>
}