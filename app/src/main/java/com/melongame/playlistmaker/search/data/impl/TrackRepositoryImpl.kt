package com.melongame.playlistmaker.search.data.impl

import com.melongame.playlistmaker.media.data.db.AppDatabase
import com.melongame.playlistmaker.search.data.NetworkClient
import com.melongame.playlistmaker.search.data.dto.TracksResponse
import com.melongame.playlistmaker.search.data.network.TrackSearchRequest
import com.melongame.playlistmaker.search.domain.api.TrackRepository
import com.melongame.playlistmaker.search.domain.models.Track
import com.melongame.playlistmaker.util.SearchResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val appDatabase: AppDatabase,
) :
    TrackRepository {

    override fun searchTrack(expression: String): Flow<SearchResult<List<Track>>> {
        return flow {
            val response = networkClient.doRequest(TrackSearchRequest(expression))
            when (response.resultCode) {
                RESULT_SUCCESS -> {
                    val favoriteTrackIds =
                        appDatabase.favoritesDao().getFavoriteTracks().first().map { it.trackId }
                            .toSet()
                    emit(SearchResult.Success((response as TracksResponse).tracks.map {
                        Track(
                            it.trackId,
                            it.trackName ?: "",
                            it.artistName ?: "",
                            it.trackTime ?: 0L,
                            it.artworkUrl100 ?: "",
                            it.collectionName ?: "",
                            it.releaseDate ?: "",
                            it.primaryGenreName ?: "",
                            it.country ?: "",
                            it.previewUrl ?: "",
                            isFavorite = favoriteTrackIds.contains(it.trackId)
                        )
                    }))
                }

                RESULT_NO_CONNECTION -> {
                    emit(SearchResult.Error(RESULT_NO_CONNECTION))
                }

                else -> {
                    emit(SearchResult.Error(RESULT_ERROR))
                }
            }
        }
    }

    private companion object {
        const val RESULT_SUCCESS = 200
        const val RESULT_NO_CONNECTION = -1
        const val RESULT_ERROR = 0
    }
}