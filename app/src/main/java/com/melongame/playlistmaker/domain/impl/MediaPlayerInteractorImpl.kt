package com.melongame.playlistmaker.domain.impl

import android.media.MediaPlayer
import com.melongame.playlistmaker.domain.api.MediaPlayerInteractor

class MediaPlayerInteractorImpl : MediaPlayerInteractor {
    private lateinit var mediaPlayer: MediaPlayer

    override fun setDataSource(url: String) {
        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
    }

    override fun prepareAsync() {
        mediaPlayer.prepareAsync()
    }

    override fun start() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun seekTo(position: Int) {
        mediaPlayer.seekTo(position)
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun setOnCompletionListener(listener: MediaPlayer.OnCompletionListener) {
        mediaPlayer.setOnCompletionListener(listener)
    }

    override fun release() {
        mediaPlayer.release()
    }

}