package com.melongame.playlistmaker.media.data.impl

import android.util.Log
import com.melongame.playlistmaker.media.data.converters.TrackDbConverter
import com.melongame.playlistmaker.media.data.db.AppDatabase
import com.melongame.playlistmaker.media.data.db.entity.TrackEntity
import com.melongame.playlistmaker.media.domain.api.FavoritesRepository
import com.melongame.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConverter: TrackDbConverter,
) : FavoritesRepository {
    override fun getFavoriteTracks(): Flow<List<Track>> {
        return appDatabase.trackDao().getFavoriteTracks()
            .map { entities ->
                Log.d("FavoritesRepository", "Получен(-о) ${entities.size} трек(-ов) из базы")
                entities.map { trackDbConverter.map(it) }
            }
    }

    override suspend fun getFavoriteTrack(trackId: Long): Int {
        return appDatabase.trackDao().getFavoriteTrack(trackId)
    }

    override fun convertToTrackEntity(track: Track): TrackEntity {
        return trackDbConverter.map(track)
    }

    override suspend fun deleteFromFavorites(trackEntity: TrackEntity) {
        Log.d("FavoritesRepository", "Трек удален из избранных: ${trackEntity.trackId}")
        appDatabase.trackDao().deleteFromFavorites(trackEntity)
    }

    override suspend fun addToFavorites(trackEntity: TrackEntity) {
        Log.d("FavoritesRepository", "Трек добавлен к избранным: ${trackEntity.trackId}")
        appDatabase.trackDao().addToFavorites(trackEntity)
    }
}