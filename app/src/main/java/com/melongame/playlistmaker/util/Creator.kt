package com.melongame.playlistmaker.util

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.melongame.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.melongame.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.melongame.playlistmaker.player.domain.api.MediaPlayerRepository
import com.melongame.playlistmaker.player.domain.impl.MediaPlayerInteractorImpl
import com.melongame.playlistmaker.search.data.impl.SearchHistoryRepositoryImpl
import com.melongame.playlistmaker.search.data.impl.TrackRepositoryImpl
import com.melongame.playlistmaker.search.data.network.RetrofitNetworkClient
import com.melongame.playlistmaker.search.domain.api.SearchHistoryRepository
import com.melongame.playlistmaker.search.domain.api.TrackInteractor
import com.melongame.playlistmaker.search.domain.api.TrackRepository
import com.melongame.playlistmaker.search.domain.impl.TrackInteractorImpl
import com.melongame.playlistmaker.settings.data.SettingsRepository
import com.melongame.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.melongame.playlistmaker.settings.domain.SettingsInteractor
import com.melongame.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.melongame.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.melongame.playlistmaker.sharing.domain.ExternalNavigator
import com.melongame.playlistmaker.sharing.domain.SharingInteractor
import com.melongame.playlistmaker.sharing.domain.impl.SharingInteractorImpl

object Creator {
    private fun getTrackRepository(context: Context): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient(context))
    }

    private fun getHistoryRepository(sharedPref: SharedPreferences): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(sharedPref)
    }

    fun provideTrackInteractor(context: Context, sharedPref: SharedPreferences): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository(context), getHistoryRepository(sharedPref))
    }

    private fun getAudioPlayerRepository(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl()
    }

    fun provideAudioPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(getAudioPlayerRepository())
    }

    fun getMediaPlayer(): MediaPlayer {
        return MediaPlayer()
    }

    private fun getExternalNavigator(application: Application): ExternalNavigator {
        return ExternalNavigatorImpl(application)
    }

    fun provideSharingInteractor(application: Application): SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator(application))
    }

    private fun getSettingsRepository(application: Application): SettingsRepository {
        return SettingsRepositoryImpl(application)
    }

    fun provideSettingsInteractor(application: Application): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(application))
    }

}