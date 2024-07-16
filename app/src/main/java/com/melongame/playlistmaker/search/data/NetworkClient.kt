package com.melongame.playlistmaker.search.data

import com.melongame.playlistmaker.search.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response

}