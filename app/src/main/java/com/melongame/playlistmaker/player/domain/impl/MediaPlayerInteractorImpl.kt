package com.melongame.playlistmaker.player.domain.impl

import com.melongame.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.melongame.playlistmaker.player.domain.api.MediaPlayerRepository


class MediaPlayerInteractorImpl(private val mediaPlayer: MediaPlayerRepository) :
    MediaPlayerInteractor {

    override fun currentPosition(): Int {
        return mediaPlayer.currentPosition()
    }

    override fun preparePlayer(
        url: String?,
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

    override fun release() {
        mediaPlayer.release()
    }
}