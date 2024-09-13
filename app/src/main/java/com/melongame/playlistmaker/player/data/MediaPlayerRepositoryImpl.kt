package com.melongame.playlistmaker.player.data

import android.media.MediaPlayer
import com.melongame.playlistmaker.player.domain.api.MediaPlayerRepository

class MediaPlayerRepositoryImpl(private var mediaPlayer: MediaPlayer) : MediaPlayerRepository {

    override fun currentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun preparePlayer(
        url: String?,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit,
    ) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            onPrepared()
        }
        mediaPlayer.setOnCompletionListener {
            onCompletion()
        }

    }

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun release() {
        mediaPlayer.reset()
    }

}