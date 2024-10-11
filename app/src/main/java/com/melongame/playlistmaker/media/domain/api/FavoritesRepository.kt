package com.melongame.playlistmaker.media.domain.api

import com.melongame.playlistmaker.media.data.db.entity.TrackEntity
import com.melongame.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    suspend fun addToFavorites(trackEntity: TrackEntity)
    fun convertToTrackEntity(track: Track): TrackEntity
    suspend fun deleteFromFavorites(trackEntity: TrackEntity)
    fun getFavoriteTracks(): Flow<List<Track>>
    suspend fun getFavoriteTrack(trackId: Long): Int
}