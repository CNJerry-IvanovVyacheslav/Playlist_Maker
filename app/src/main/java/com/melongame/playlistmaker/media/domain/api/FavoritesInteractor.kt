package com.melongame.playlistmaker.media.domain.api

import com.melongame.playlistmaker.media.data.db.entity.FavoritesTrackEntity
import com.melongame.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {

    suspend fun addToFavorites(favoritesTrackEntity: FavoritesTrackEntity)

    fun convertToTrackEntity(track: Track): FavoritesTrackEntity

    suspend fun deleteFromFavorites(favoritesTrackEntity: FavoritesTrackEntity)

    fun getFavoriteTracks(): Flow<List<Track>>

    suspend fun getFavoriteTrack(trackId: Long): Int
}