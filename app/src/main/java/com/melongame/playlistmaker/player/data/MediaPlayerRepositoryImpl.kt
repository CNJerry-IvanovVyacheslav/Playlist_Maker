package com.melongame.playlistmaker.player.data

import com.google.gson.Gson
import com.melongame.playlistmaker.util.Creator
import com.melongame.playlistmaker.player.domain.api.MediaPlayerRepository
import com.melongame.playlistmaker.player.domain.models.MediaPlayerState
import com.melongame.playlistmaker.search.domain.models.Track

class MediaPlayerRepositoryImpl : MediaPlayerRepository {

    private val mediaPlayer = Creator.getMediaPlayer()
    private var playerState = MediaPlayerState.DEFAULT
    override fun getTrackFromJson(trackJsonString: String?): Track {
        return Gson().fromJson(trackJsonString, Track::class.java)
    }


    override fun currentPosition(): Int {
        return if (playerState != MediaPlayerState.DEFAULT && playerState != MediaPlayerState.PREPARED) {
            mediaPlayer.currentPosition
        } else {
            0
        }
    }

    override fun preparePlayer(
        url: String,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit,
    ) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = MediaPlayerState.PREPARED
            onPrepared()
        }
        mediaPlayer.setOnCompletionListener {
            playerState = MediaPlayerState.PREPARED
            onCompletion()
        }

    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = MediaPlayerState.PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = MediaPlayerState.PAUSED
    }

    override fun playbackControl(): Boolean {
        return when (playerState) {
            MediaPlayerState.PLAYING -> {
                pausePlayer()
                false
            }

            MediaPlayerState.PREPARED, MediaPlayerState.PAUSED -> {
                startPlayer()
                true
            }

            else -> false
        }
    }

    override fun release() {
        mediaPlayer.release()
        playerState = MediaPlayerState.DEFAULT
    }

}