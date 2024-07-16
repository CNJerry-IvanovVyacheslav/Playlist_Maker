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
            200 -> {
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

            -1 -> {
                SearchResult.Error(-1)
            }

            else -> {
                SearchResult.Error(0)
            }
        }
    }
}