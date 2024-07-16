package com.melongame.playlistmaker.search.data.network

import com.melongame.playlistmaker.search.data.dto.TracksResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search?entity=song")
    fun getTracks(@Query("term") term: String): Call<TracksResponse>
}