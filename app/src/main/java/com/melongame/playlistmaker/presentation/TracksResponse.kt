package com.melongame.playlistmaker.presentation

import com.melongame.playlistmaker.domain.models.Track

data class TracksResponse(
    val resultCount: Int,
    val results: List<Track>
)