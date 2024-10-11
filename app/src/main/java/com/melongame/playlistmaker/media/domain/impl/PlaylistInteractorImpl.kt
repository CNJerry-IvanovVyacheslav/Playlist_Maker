package com.melongame.playlistmaker.media.domain.impl

import com.melongame.playlistmaker.media.domain.api.PlaylistInteractor
import com.melongame.playlistmaker.media.domain.api.PlaylistRepository
import com.melongame.playlistmaker.media.domain.models.Playlist
import com.melongame.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val repository: PlaylistRepository) : PlaylistInteractor {
    override suspend fun addTrackToPlaylist(track: Track, playlistId: Long) {
        repository.addTrackToPlaylist(track, playlistId)
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return repository.getAllPlaylists()
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        repository.updatePlaylist(playlist)
    }

}