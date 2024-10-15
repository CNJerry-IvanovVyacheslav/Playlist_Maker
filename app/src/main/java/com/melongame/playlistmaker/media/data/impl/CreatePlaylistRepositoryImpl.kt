package com.melongame.playlistmaker.media.data.impl

import android.content.Context
import android.net.Uri
import com.melongame.playlistmaker.media.data.converters.PlaylistDbConverter
import com.melongame.playlistmaker.media.data.db.AppDatabase
import com.melongame.playlistmaker.media.domain.api.CreatePlaylistRepository
import com.melongame.playlistmaker.media.domain.models.Playlist
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CreatePlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConverter,
) : CreatePlaylistRepository {
    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().addPlaylist(playlistDbConvertor.map(playlist))
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().updatePlaylist(playlistDbConvertor.map(playlist))
    }

    override fun copyImageToPrivateStorage(context: Context, uri: Uri): Uri? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File(context.filesDir, "playlist_cover_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(file)

            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            Uri.fromFile(file)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}