package com.melongame.playlistmaker

import android.media.MediaPlayer
import com.melongame.playlistmaker.data.MediaPlayerRepositoryImpl
import com.melongame.playlistmaker.domain.api.MediaPlayerInteractor
import com.melongame.playlistmaker.domain.api.MediaPlayerRepository
import com.melongame.playlistmaker.domain.impl.MediaPlayerInteractorImpl

object Creator {

    fun getMediaPlayer(): MediaPlayer {
        return MediaPlayer()
    }

    private fun getMediaPlayerRepository(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl()
    }

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(getMediaPlayerRepository())
    }
}