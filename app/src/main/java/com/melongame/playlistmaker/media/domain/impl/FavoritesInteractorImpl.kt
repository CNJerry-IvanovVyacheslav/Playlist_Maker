package com.melongame.playlistmaker.media.domain.impl

import com.melongame.playlistmaker.media.data.db.entity.FavoritesTrackEntity
import com.melongame.playlistmaker.media.domain.api.FavoritesInteractor
import com.melongame.playlistmaker.media.domain.api.FavoritesRepository
import com.melongame.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(private val favoritesRepository: FavoritesRepository) :
    FavoritesInteractor {
    override suspend fun addToFavorites(favoritesTrackEntity: FavoritesTrackEntity) {
        return favoritesRepository.addToFavorites(favoritesTrackEntity)
    }

    override fun convertToTrackEntity(track: Track): FavoritesTrackEntity {
        return favoritesRepository.convertToTrackEntity(track)
    }

    override suspend fun deleteFromFavorites(favoritesTrackEntity: FavoritesTrackEntity) {
        return favoritesRepository.deleteFromFavorites(favoritesTrackEntity)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return favoritesRepository.getFavoriteTracks()
    }

    override suspend fun getFavoriteTrack(trackId: Long): Int {
        return favoritesRepository.getFavoriteTrack(trackId)
    }
}