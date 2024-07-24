package com.melongame.playlistmaker.search.data.impl

import com.melongame.playlistmaker.search.data.NetworkClient
import com.melongame.playlistmaker.search.data.dto.TracksResponse
import com.melongame.playlistmaker.search.data.network.TrackSearchRequest
import com.melongame.playlistmaker.search.domain.api.TrackRepository
import com.melongame.playlistmaker.search.domain.models.Track
import com.melongame.playlistmaker.util.SearchResult

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override fun searchTrack(expression: String): SearchResult<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        return when (response.resultCode) {
            RESULT_SUCCESS -> {
                SearchResult.Success((response as TracksResponse).tracks.map {
                    Track(
                        it.trackName ?: "",
                        it.artistName ?: "",
                        it.trackTime ?: 0L,
                        it.artworkUrl100 ?: "",
                        it.collectionName ?: "",
                        it.releaseDate ?: "",
                        it.primaryGenreName ?: "",
                        it.country ?: "",
                        it.previewUrl ?: ""
                    )
                })
            }

            RESULT_NO_CONNECTION -> {
                SearchResult.Error(RESULT_NO_CONNECTION)
            }

            else -> {
                SearchResult.Error(RESULT_ERROR)
            }
        }
    }

    private companion object {
        const val RESULT_SUCCESS = 200
        const val RESULT_NO_CONNECTION = -1
        const val RESULT_ERROR = 0
    }
}