package com.melongame.playlistmaker.di

import com.melongame.playlistmaker.media.domain.api.CreatePlaylistInteractor
import com.melongame.playlistmaker.media.domain.api.FavoritesInteractor
import com.melongame.playlistmaker.media.domain.api.PlaylistInteractor
import com.melongame.playlistmaker.media.domain.impl.CreatePlaylistInteractorImpl
import com.melongame.playlistmaker.media.domain.impl.FavoritesInteractorImpl
import com.melongame.playlistmaker.media.domain.impl.PlaylistInteractorImpl
import com.melongame.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.melongame.playlistmaker.player.domain.impl.MediaPlayerInteractorImpl
import com.melongame.playlistmaker.search.domain.api.TrackInteractor
import com.melongame.playlistmaker.search.domain.impl.TrackInteractorImpl
import com.melongame.playlistmaker.settings.domain.SettingsInteractor
import com.melongame.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.melongame.playlistmaker.sharing.domain.SharingInteractor
import com.melongame.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<TrackInteractor> {
        TrackInteractorImpl(get(), get())
    }

    factory<MediaPlayerInteractor> {
        MediaPlayerInteractorImpl(get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    single<FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }

    single<CreatePlaylistInteractor> {
        CreatePlaylistInteractorImpl(get())
    }

    single<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }
}