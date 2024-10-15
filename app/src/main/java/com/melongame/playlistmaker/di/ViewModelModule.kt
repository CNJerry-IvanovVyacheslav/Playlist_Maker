package com.melongame.playlistmaker.di

import com.melongame.playlistmaker.media.ui.view_model.CreatePlaylistViewModel
import com.melongame.playlistmaker.media.ui.view_model.FavoritesFragmentViewModel
import com.melongame.playlistmaker.media.ui.view_model.MediaViewModel
import com.melongame.playlistmaker.media.ui.view_model.PlaylistsFragmentViewModel
import com.melongame.playlistmaker.player.ui.view_model.MediaPlayerViewModel
import com.melongame.playlistmaker.search.ui.view_model.SearchViewModel
import com.melongame.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel<SearchViewModel> {
        SearchViewModel(get())
    }
    viewModel<MediaPlayerViewModel> {
        MediaPlayerViewModel(get(), get(), get())
    }
    viewModel<SettingsViewModel> {
        SettingsViewModel(get(), get())
    }
    viewModel<MediaViewModel> {
        MediaViewModel()
    }
    viewModel<FavoritesFragmentViewModel> {
        FavoritesFragmentViewModel(get())
    }
    viewModel<PlaylistsFragmentViewModel> {
        PlaylistsFragmentViewModel(get())
    }
    viewModel {
        CreatePlaylistViewModel(get())
    }
}