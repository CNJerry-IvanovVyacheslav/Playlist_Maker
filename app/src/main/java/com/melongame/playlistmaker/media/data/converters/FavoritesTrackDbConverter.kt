package com.melongame.playlistmaker.media.data.converters

import com.melongame.playlistmaker.media.data.db.entity.FavoritesTrackEntity
import com.melongame.playlistmaker.search.domain.models.Track

class FavoritesTrackDbConverter {
    fun map(track: FavoritesTrackEntity): Track {
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

    fun map(track: Track): FavoritesTrackEntity {
        return FavoritesTrackEntity(
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