package com.melongame.playlistmaker.media.ui.view_model

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melongame.playlistmaker.media.domain.api.CreatePlaylistInteractor
import com.melongame.playlistmaker.media.domain.models.Playlist
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(
    private val createPlaylistInteractor: CreatePlaylistInteractor,
) : ViewModel() {

    private val _playlistCreated = MutableLiveData<Boolean>()
    val playlistCreated: LiveData<Boolean> get() = _playlistCreated

    fun createPlaylist(name: String, description: String?, coverImagePath: String?) {
        viewModelScope.launch {
            try {
                val playlist = Playlist(
                    id = 0,
                    name = name,
                    description = description,
                    coverImagePath = coverImagePath,
                    trackIds = "",
                    trackCount = 0
                )

                createPlaylistInteractor.addPlaylist(playlist)

                _playlistCreated.postValue(true)
            } catch (e: Exception) {
                e.printStackTrace()
                _playlistCreated.postValue(false)
            }
        }
    }

    fun copyImageToPrivateStorage(context: Context, uri: Uri): Uri? {
        return createPlaylistInteractor.copyImageToPrivateStorage(context, uri)
    }
}