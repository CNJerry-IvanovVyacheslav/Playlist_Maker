package com.melongame.playlistmaker.search.ui.view_model

import com.melongame.playlistmaker.search.domain.models.Track

sealed interface SearchState {

    object Default : SearchState

    data object Loading : SearchState

    data class TrackList(val tracks: List<Track>) : SearchState

    data class HistoryTrackList(val tracks: List<Track>) : SearchState

    data object ServerError : SearchState

    data object NotFound : SearchState
}