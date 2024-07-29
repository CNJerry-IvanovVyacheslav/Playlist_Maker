package com.melongame.playlistmaker.di

import com.melongame.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.melongame.playlistmaker.player.domain.api.MediaPlayerRepository
import com.melongame.playlistmaker.search.data.impl.SearchHistoryRepositoryImpl
import com.melongame.playlistmaker.search.data.impl.TrackRepositoryImpl
import com.melongame.playlistmaker.search.domain.api.SearchHistoryRepository
import com.melongame.playlistmaker.search.domain.api.TrackRepository
import com.melongame.playlistmaker.settings.data.SettingsRepository
import com.melongame.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.melongame.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.melongame.playlistmaker.sharing.domain.ExternalNavigator
import org.koin.dsl.module

val repositoryModule = module {

    single<TrackRepository> {
        TrackRepositoryImpl(get(), get())
    }

    factory<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(), get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(get())
    }
}