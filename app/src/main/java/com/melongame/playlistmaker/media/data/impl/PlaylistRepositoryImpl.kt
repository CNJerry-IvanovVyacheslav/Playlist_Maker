package com.melongame.playlistmaker.media.data.impl

import com.melongame.playlistmaker.media.data.converters.PlaylistDbConverter
import com.melongame.playlistmaker.media.data.converters.PlaylistTrackDbConverter
import com.melongame.playlistmaker.media.data.converters.TrackDbConverter
import com.melongame.playlistmaker.media.data.db.AppDatabase
import com.melongame.playlistmaker.media.domain.api.PlaylistRepository
import com.melongame.playlistmaker.media.domain.models.Playlist
import com.melongame.playlistmaker.media.domain.models.PlaylistTrack
import com.melongame.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistTrackDbConverter: PlaylistTrackDbConverter,
    private val playlistDbConverter: PlaylistDbConverter,
    private val trackDbConverter: TrackDbConverter,
) : PlaylistRepository {
    override suspend fun addTrackToPlaylist(track: Track, playlistId: Long) {
        val trackEntity = trackDbConverter.map(track)
        appDatabase.trackDao().insertTrack(trackEntity)

        val playlistTrack = PlaylistTrack(track.trackId, playlistId)
        appDatabase.playlistTrackDao()
            .addTrackToPlaylist(playlistTrackDbConverter.map(playlistTrack))

    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return appDatabase.playlistDao().getAllPlaylists()
            .map { entities ->
                entities.map { playlistDbConverter.map(it) }
            }
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        val playlistEntity = playlistDbConverter.map(playlist)
        appDatabase.playlistDao().updatePlaylist(playlistEntity)
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        appDatabase.playlistDao().deletePlaylistById(playlistId)
        appDatabase.trackDao().deleteUnusedTracks()
    }
}