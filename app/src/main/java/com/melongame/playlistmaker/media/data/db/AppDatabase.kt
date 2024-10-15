package com.melongame.playlistmaker.media.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.melongame.playlistmaker.media.data.db.dao.PlaylistDao
import com.melongame.playlistmaker.media.data.db.dao.PlaylistTrackDao
import com.melongame.playlistmaker.media.data.db.dao.TrackDao
import com.melongame.playlistmaker.media.data.db.entity.PlaylistEntity
import com.melongame.playlistmaker.media.data.db.entity.PlaylistTrackEntity
import com.melongame.playlistmaker.media.data.db.entity.TrackEntity

@Database(
    version = 3,
    exportSchema = false,
    entities = [TrackEntity::class, PlaylistEntity::class, PlaylistTrackEntity::class]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun playlistTrackDao(): PlaylistTrackDao

}