package com.melongame.playlistmaker.player.domain.api

import com.melongame.playlistmaker.search.domain.models.Track

interface MediaPlayerInteractor {

    fun getTrackFromJson(trackJsonString: String?): Track
    fun currentPosition(): Int
    fun preparePlayer(
        url: String?,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit,
    )

    fun startPlayer()
    fun pausePlayer()
    fun playbackControl(): Boolean
    fun release()

}