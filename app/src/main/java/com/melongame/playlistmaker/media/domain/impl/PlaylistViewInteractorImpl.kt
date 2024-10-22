package com.melongame.playlistmaker.media.domain.impl

import com.melongame.playlistmaker.media.domain.api.PlaylistViewInteractor
import com.melongame.playlistmaker.media.domain.api.PlaylistViewRepository
import com.melongame.playlistmaker.media.domain.models.Playlist
import com.melongame.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistViewInteractorImpl(private val repository: PlaylistViewRepository) :
    PlaylistViewInteractor {
    override fun getPlaylistById(playlistId: Long): Flow<Playlist?> {
        return repository.getPlaylistById(playlistId)
    }

    override fun getTracksForPlaylist(playlistId: Long): Flow<List<Track>> {
        return repository.getTracksForPlaylist(playlistId)
    }

    override suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long) {
        return repository.deleteTrackFromPlaylist(trackId, playlistId)
    }
}