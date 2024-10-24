package com.melongame.playlistmaker.media.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.melongame.playlistmaker.media.domain.api.PlaylistInteractor
import com.melongame.playlistmaker.media.domain.api.PlaylistViewInteractor
import com.melongame.playlistmaker.media.domain.models.Playlist
import com.melongame.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class PlaylistViewingViewModel(
    private val interactor: PlaylistViewInteractor,
    private val playlistInteractor: PlaylistInteractor,
) : ViewModel() {

    private val _playlistId = MutableLiveData<Long>()
    private val playlistId: LiveData<Long> = _playlistId

    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> get() = _tracks

    val playlist: LiveData<Playlist?> = playlistId.switchMap { id ->
        interactor.getPlaylistById(id).asLiveData()
    }
    private var isClickAllowed = true

    fun loadPlaylist(playlistId: Long) {
        _playlistId.value = playlistId
        loadTracksForPlaylist(playlistId)
    }

    private fun loadTracksForPlaylist(playlistId: Long) {
        viewModelScope.launch {
            val tracksFlow = interactor.getTracksForPlaylist(playlistId)
            tracksFlow.collect { tracks ->
                _tracks.postValue(tracks)
            }
        }
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModelScope.launch {
                delay(FavoritesFragmentViewModel.CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
    }

    fun createShareText(playlist: Playlist?): String {
        val tracks = tracks.value.orEmpty()
        val trackListText = tracks.mapIndexed { index, track ->
            "${index + ONE}. ${track.artistName} - ${track.trackName} (${formatDuration(track.trackTime)})"
        }.joinToString("\n")

        val playlistName = playlist?.name
        val playlistDescription = playlist?.description?.takeIf { it.isNotBlank() } ?: ""

        return buildString {
            append(playlistName)
            if (playlistDescription.isNotBlank()) {
                append("\n$playlistDescription")
            }
            append("\n\n$trackListText")
        }.trim()
    }

    private fun formatDuration(trackTime: Long): String {
        val minutes = trackTime / MILLS_TO_MINUTES_CHANGE
        val seconds = (trackTime % MILLS_TO_MINUTES_CHANGE) / ONE_HUNDRED
        return String.format("%d:%02d", minutes, seconds)
    }

    fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long) {
        viewModelScope.launch {
            interactor.deleteTrackFromPlaylist(trackId, playlistId)
            val playlist = interactor.getPlaylistById(playlistId).firstOrNull()

            playlist?.let { it ->
                val updatedTrackIds = it.trackIds.split(",")
                    .filter { id -> id.isNotEmpty() && id.toLongOrNull() != null && id.toLong() != trackId }
                    .joinToString(",")
                val updatedTrackCount = updatedTrackIds.split(",").filter { it.isNotEmpty() }.size

                val updatedPlaylist = it.copy(
                    trackIds = updatedTrackIds,
                    trackCount = updatedTrackCount
                )
                playlistInteractor.updatePlaylist(updatedPlaylist)
            }
        }
    }

    fun deletePlaylist(playlistId: Long) {
        viewModelScope.launch {
            playlistInteractor.deletePlaylist(playlistId)
        }
    }

    companion object {
        private const val ONE = 1
        private const val ONE_HUNDRED = 1000
        private const val MILLS_TO_MINUTES_CHANGE = 60000
    }
}