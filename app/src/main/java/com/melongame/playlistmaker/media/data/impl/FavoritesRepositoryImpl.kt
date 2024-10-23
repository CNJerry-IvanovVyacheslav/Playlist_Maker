package com.melongame.playlistmaker.media.data.impl

import android.util.Log
import com.melongame.playlistmaker.media.data.converters.FavoritesTrackDbConverter
import com.melongame.playlistmaker.media.data.db.AppDatabase
import com.melongame.playlistmaker.media.data.db.entity.FavoritesTrackEntity
import com.melongame.playlistmaker.media.domain.api.FavoritesRepository
import com.melongame.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConverter: FavoritesTrackDbConverter,
) : FavoritesRepository {
    override fun getFavoriteTracks(): Flow<List<Track>> {
        return appDatabase.favoritesDao().getFavoriteTracks()
            .map { entities ->
                Log.d("FavoritesRepository", "Получен(-о) ${entities.size} трек(-ов) из базы")
                entities.map { trackDbConverter.map(it) }
            }
    }

    override suspend fun getFavoriteTrack(trackId: Long): Int {
        return appDatabase.favoritesDao().getFavoriteTrack(trackId)
    }

    override fun convertToTrackEntity(track: Track): FavoritesTrackEntity {
        return trackDbConverter.map(track)
    }

    override suspend fun deleteFromFavorites(favoritesTrackEntity: FavoritesTrackEntity) {
        Log.d("FavoritesRepository", "Трек удален из избранных: ${favoritesTrackEntity.trackId}")
        appDatabase.favoritesDao().deleteFromFavorites(favoritesTrackEntity)
    }

    override suspend fun addToFavorites(favoritesTrackEntity: FavoritesTrackEntity) {
        Log.d("FavoritesRepository", "Трек добавлен к избранным: ${favoritesTrackEntity.trackId}")
        appDatabase.favoritesDao().addToFavorites(favoritesTrackEntity)
    }
}