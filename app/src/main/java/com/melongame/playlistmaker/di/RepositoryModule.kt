package com.melongame.playlistmaker.di

import com.melongame.playlistmaker.media.data.converters.FavoritesTrackDbConverter
import com.melongame.playlistmaker.media.data.converters.PlaylistDbConverter
import com.melongame.playlistmaker.media.data.converters.PlaylistTrackDbConverter
import com.melongame.playlistmaker.media.data.converters.TrackDbConverter
import com.melongame.playlistmaker.media.data.impl.CreatePlaylistRepositoryImpl
import com.melongame.playlistmaker.media.data.impl.FavoritesRepositoryImpl
import com.melongame.playlistmaker.media.data.impl.PlaylistRepositoryImpl
import com.melongame.playlistmaker.media.data.impl.PlaylistViewRepositoryImpl
import com.melongame.playlistmaker.media.domain.api.CreatePlaylistRepository
import com.melongame.playlistmaker.media.domain.api.FavoritesRepository
import com.melongame.playlistmaker.media.domain.api.PlaylistRepository
import com.melongame.playlistmaker.media.domain.api.PlaylistViewRepository
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
        SearchHistoryRepositoryImpl(get(), get(), get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(get())
    }

    factory { FavoritesTrackDbConverter() }
    factory { PlaylistDbConverter() }
    factory { PlaylistTrackDbConverter() }
    factory { TrackDbConverter() }

    single<FavoritesRepository> {
        FavoritesRepositoryImpl(get(), get())
    }

    single<CreatePlaylistRepository> {
        CreatePlaylistRepositoryImpl(get(), get())
    }
    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get(), get(), get())
    }
    single<PlaylistViewRepository> {
        PlaylistViewRepositoryImpl(get(), get(), get())
    }
}