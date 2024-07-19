package com.melongame.playlistmaker.player.ui.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.melongame.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.melongame.playlistmaker.search.domain.models.Track
import com.melongame.playlistmaker.util.Creator

class MediaPlayerViewModel(private val interactor: MediaPlayerInteractor) : ViewModel() {

    private val playerState = MutableLiveData<PlayerState>()
    val pState: LiveData<PlayerState> get() = playerState

    init {
        playerState.value = PlayerState()
    }

    fun setTrack(trackJsonString: String?) {
        playerState.value = playerState.value?.copy(
            track = interactor.getTrackFromJson(trackJsonString)
        )
    }

    fun preparePlayer(url: String?) {
        if (url != null) {
            interactor.preparePlayer(url, {
                playerState.value = playerState.value?.copy(isPrepared = true)
            }, {
                playerState.value =
                    playerState.value?.copy(
                        isPlaying = false,
                        isPrepared = true,
                        currentPosition = 0
                    )
                Log.i("TimeChanged", "Время обновилось на 0 после завершения трека!")
            })
        }
    }


    fun pausePlayer() {
        interactor.pausePlayer()
        playerState.value = playerState.value?.copy(isPlaying = false)
    }

    fun playbackControl() {
        val isPlaying = interactor.playbackControl()
        playerState.value = playerState.value?.copy(isPlaying = isPlaying)
    }

    fun updateCurrentPosition() {
        playerState.value = playerState.value?.copy(currentPosition = interactor.currentPosition())
    }

    fun release() {
        interactor.release()
        playerState.value =
            playerState.value?.copy(isPlaying = false, isPrepared = false, currentPosition = 0)
        Log.i("TimeChanged", "Время обновилось на 0 после выхода!")
    }

    fun getCollectionNameVisibility(track: Track): Boolean {
        return !(track.collectionName.isEmpty() || track.collectionName.contains("Single"))
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MediaPlayerViewModel(
                    Creator.provideAudioPlayerInteractor()
                )
            }
        }
    }
}