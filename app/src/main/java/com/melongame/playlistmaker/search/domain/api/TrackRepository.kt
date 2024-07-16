package com.melongame.playlistmaker.search.domain.api

import com.melongame.playlistmaker.search.domain.models.Track
import com.melongame.playlistmaker.util.SearchResult

interface TrackRepository {
    fun searchTrack(expression: String): SearchResult<List<Track>>
}