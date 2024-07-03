package com.melongame.playlistmaker.data.network

import com.melongame.playlistmaker.data.dto.TracksResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApiService {
    @GET("search")
    fun search(
        @Query("term") text: String,
        @Query("media") media: String = "music",
        @Query("entity") entity: String = "song",
    ): Call<TracksResponse>
}