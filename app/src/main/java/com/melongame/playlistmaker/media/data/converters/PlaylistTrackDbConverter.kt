package com.melongame.playlistmaker.media.data.converters

import com.melongame.playlistmaker.media.data.db.entity.PlaylistTrackEntity
import com.melongame.playlistmaker.media.domain.models.PlaylistTrack

class PlaylistTrackDbConverter {
    fun map(playlistTrack: PlaylistTrackEntity): PlaylistTrack {
        return PlaylistTrack(
            playlistTrack.trackId,
            playlistTrack.playlistId
        )
    }

    fun map(playlistTrack: PlaylistTrack): PlaylistTrackEntity {
        return PlaylistTrackEntity(
            playlistTrack.trackId,
            playlistTrack.playlistId,
            addedAt = System.currentTimeMillis()
        )
    }
}