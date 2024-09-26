package com.melongame.playlistmaker.media.ui.view_model

import com.melongame.playlistmaker.search.domain.models.Track

sealed interface FavoritesState {
    data class Content(
        val tracks: List<Track>,
    ) : FavoritesState

    data object Empty : FavoritesState
}