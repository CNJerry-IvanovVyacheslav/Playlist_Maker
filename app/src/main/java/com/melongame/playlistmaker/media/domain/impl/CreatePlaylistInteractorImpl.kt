package com.melongame.playlistmaker.media.domain.impl

import android.content.Context
import android.net.Uri
import com.melongame.playlistmaker.media.domain.api.CreatePlaylistInteractor
import com.melongame.playlistmaker.media.domain.api.CreatePlaylistRepository
import com.melongame.playlistmaker.media.domain.models.Playlist

class CreatePlaylistInteractorImpl(private val repository: CreatePlaylistRepository) :
    CreatePlaylistInteractor {
    override suspend fun addPlaylist(playlist: Playlist) {
        return repository.addPlaylist(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        return repository.updatePlaylist(playlist)
    }

    override suspend fun getPlaylistById(playlistId: Long): Playlist? {
        return repository.getPlaylistById(playlistId)
    }

    override fun copyImageToPrivateStorage(context: Context, uri: Uri): Uri? {
        return repository.copyImageToPrivateStorage(context, uri)
    }

}