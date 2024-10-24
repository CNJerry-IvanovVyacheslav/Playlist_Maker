package com.melongame.playlistmaker.media.data.db.entity

import androidx.room.Entity

@Entity(tableName = "playlist_tracks", primaryKeys = ["trackId", "playlistId"])
data class PlaylistTrackEntity(
    val trackId: Long,
    val playlistId: Long,
    val addedAt: Long,
)