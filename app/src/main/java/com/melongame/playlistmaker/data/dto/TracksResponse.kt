package com.melongame.playlistmaker.data.dto

import com.melongame.playlistmaker.domain.models.Track

data class TracksResponse(
    val resultCount: Int,
    val results: List<Track>,
) : Response()