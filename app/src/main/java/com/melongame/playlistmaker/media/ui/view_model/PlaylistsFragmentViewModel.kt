package com.melongame.playlistmaker.media.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melongame.playlistmaker.media.domain.api.PlaylistInteractor
import com.melongame.playlistmaker.media.domain.models.Playlist
import kotlinx.coroutines.launch

class PlaylistsFragmentViewModel(
    private val playlistInteractor: PlaylistInteractor,
) : ViewModel() {

    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> get() = _playlists

    init {
        fetchPlaylists()
    }

    private fun fetchPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getAllPlaylists().collect { playlistList ->
                _playlists.postValue(playlistList)
            }
        }
    }
}