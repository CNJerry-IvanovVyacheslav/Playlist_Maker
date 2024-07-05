package com.melongame.playlistmaker.data

import com.melongame.playlistmaker.domain.api.TrackAdapterRepository
import com.melongame.playlistmaker.domain.models.Track

class TrackAdapterRepositoryImpl : TrackAdapterRepository {
    private lateinit var tracks: MutableList<Track>
    override fun updateTracks(newTracks: MutableList<Track>) {
        this.tracks = newTracks
    }
}