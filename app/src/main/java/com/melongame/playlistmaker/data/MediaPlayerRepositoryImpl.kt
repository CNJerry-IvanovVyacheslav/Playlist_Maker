package com.melongame.playlistmaker.data

import com.melongame.playlistmaker.Creator
import com.melongame.playlistmaker.domain.api.MediaPlayerRepository

class MediaPlayerRepositoryImpl : MediaPlayerRepository {

    private val mediaPlayer = Creator.getMediaPlayer()

    override fun setDataSource(url: String) {
        mediaPlayer.setDataSource(url)
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

    override fun release() {
        mediaPlayer.release()
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        mediaPlayer.setOnCompletionListener { listener() }
    }

}