package com.melongame.playlistmaker.media.data.converters

import com.melongame.playlistmaker.media.data.db.entity.TrackEntity
import com.melongame.playlistmaker.search.domain.models.Track

class TrackDbConverter {
    fun map(track: TrackEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }

    fun map(track: Track): TrackEntity {
        return TrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            addedAt = System.currentTimeMillis()
        )
    }
}