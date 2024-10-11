package com.melongame.playlistmaker.media.data.converters

import com.melongame.playlistmaker.media.data.db.entity.PlaylistEntity
import com.melongame.playlistmaker.media.domain.models.Playlist

class PlaylistDbConverter {
    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlist.id,
            playlist.name,
            playlist.description,
            playlist.coverImagePath,
            playlist.trackIds,
            playlist.trackCount
        )
    }

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.id,
            playlist.name,
            playlist.description,
            playlist.coverImagePath,
            playlist.trackIds,
            playlist.trackCount
        )
    }
}