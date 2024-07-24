package com.melongame.playlistmaker.search.data

import com.melongame.playlistmaker.search.data.dto.Response
import com.melongame.playlistmaker.search.data.network.TrackSearchRequest

interface NetworkClient {
    fun doRequest(dto: TrackSearchRequest): Response

}