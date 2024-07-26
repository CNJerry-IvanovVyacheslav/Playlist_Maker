package com.melongame.playlistmaker.player.data

import android.media.MediaPlayer
import com.google.gson.Gson
import com.melongame.playlistmaker.player.domain.api.MediaPlayerRepository
import com.melongame.playlistmaker.player.domain.models.MediaPlayerState
import com.melongame.playlistmaker.search.domain.models.Track

class MediaPlayerRepositoryImpl(private var mediaPlayer: MediaPlayer?) : MediaPlayerRepository {

    private var playerState = MediaPlayerState.DEFAULT
    override fun getTrackFromJson(trackJsonString: String?): Track {
        return Gson().fromJson(trackJsonString, Track::class.java)
    }


    override fun currentPosition(): Int? {
        return if (playerState != MediaPlayerState.DEFAULT && playerState != MediaPlayerState.PREPARED) {
            mediaPlayer?.currentPosition
        } else {
            0
        }
    }

    override fun preparePlayer(
        url: String?,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit,
    ) {
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(url)
        mediaPlayer?.prepareAsync()
        mediaPlayer?.setOnPreparedListener {
            playerState = MediaPlayerState.PREPARED
            onPrepared.invoke()
        }
        mediaPlayer?.setOnCompletionListener {
            playerState = MediaPlayerState.PREPARED
            onCompletion.invoke()
        }

    }

    override fun startPlayer() {
        mediaPlayer?.start()
        playerState = MediaPlayerState.PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer?.pause()
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
        mediaPlayer?.release()
        mediaPlayer = null
        playerState = MediaPlayerState.DEFAULT
    }

}