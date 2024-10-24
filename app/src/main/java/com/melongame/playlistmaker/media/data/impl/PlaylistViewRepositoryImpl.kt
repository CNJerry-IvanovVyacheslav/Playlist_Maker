package com.melongame.playlistmaker.media.data.impl

import com.melongame.playlistmaker.media.data.converters.PlaylistDbConverter
import com.melongame.playlistmaker.media.data.converters.TrackDbConverter
import com.melongame.playlistmaker.media.data.db.AppDatabase
import com.melongame.playlistmaker.media.domain.api.PlaylistViewRepository
import com.melongame.playlistmaker.media.domain.models.Playlist
import com.melongame.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistViewRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConverter: TrackDbConverter,
    private val playlistDbConverter: PlaylistDbConverter,
) : PlaylistViewRepository {
    override fun getPlaylistById(playlistId: Long): Flow<Playlist?> {
        return appDatabase.playlistDao().getPlaylistByIdFlow(playlistId)
            .map { entity -> entity?.let { playlistDbConverter.map(it) } }
    }


    override fun getTracksForPlaylist(playlistId: Long): Flow<List<Track>> {
        return appDatabase.playlistTrackDao().getTracksForPlaylist(playlistId)
            .map { trackEntities ->
                trackEntities.map { trackDbConverter.map(it) }
            }
    }

    override suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long) {
        appDatabase.playlistTrackDao().deleteTrackFromPlaylist(trackId, playlistId)
        val isTrackUsed = appDatabase.playlistTrackDao().isTrackInAnyPlaylist(trackId)
        if (!isTrackUsed) {
            appDatabase.trackDao().deleteTrackById(trackId)
        }
    }
}