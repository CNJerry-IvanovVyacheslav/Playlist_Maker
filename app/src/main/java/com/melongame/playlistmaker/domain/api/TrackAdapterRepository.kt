package com.melongame.playlistmaker.domain.api

import com.melongame.playlistmaker.domain.models.Track

interface TrackAdapterRepository {
    fun updateTracks(newTracks: MutableList<Track>){
    }
}