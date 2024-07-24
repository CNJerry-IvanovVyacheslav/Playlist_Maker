package com.melongame.playlistmaker.player.domain.impl

import com.melongame.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.melongame.playlistmaker.player.domain.api.MediaPlayerRepository
import com.melongame.playlistmaker.search.domain.models.Track


class MediaPlayerInteractorImpl(private val mediaPlayer: MediaPlayerRepository) :
    MediaPlayerInteractor {
    override fun getTrackFromJson(trackJsonString: String?): Track {
        return mediaPlayer.getTrackFromJson(trackJsonString)
    }

    override fun currentPosition(): Int {
        return mediaPlayer.currentPosition()!!
    }

    override fun preparePlayer(
        url: String,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit,
    ) {
        mediaPlayer.preparePlayer(url, onPrepared, onCompletion)
    }

    override fun startPlayer() {
        mediaPlayer.startPlayer()
    }

    override fun pausePlayer() {
        mediaPlayer.pausePlayer()
    }

    override fun playbackControl(): Boolean {
        return mediaPlayer.playbackControl()
    }

    override fun release() {
        mediaPlayer.release()
    }
}