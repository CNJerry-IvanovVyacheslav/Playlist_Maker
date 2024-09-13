package com.melongame.playlistmaker.player.domain.api


interface MediaPlayerInteractor {

    fun currentPosition(): Int
    fun preparePlayer(
        url: String?,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit,
    )

    fun startPlayer()
    fun pausePlayer()
    fun release()

}