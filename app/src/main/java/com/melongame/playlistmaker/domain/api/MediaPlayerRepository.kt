package com.melongame.playlistmaker.domain.api

interface MediaPlayerRepository {
    fun setDataSource(url: String)
    fun prepareAsync()
    fun start()
    fun pause()
    fun seekTo(position: Int)
    fun getCurrentPosition(): Int
    fun isPlaying(): Boolean
    fun release()
    fun setOnCompletionListener(listener: () -> Unit)
}