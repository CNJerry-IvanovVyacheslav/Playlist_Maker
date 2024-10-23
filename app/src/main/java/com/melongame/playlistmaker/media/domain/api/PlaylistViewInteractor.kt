package com.melongame.playlistmaker.media.domain.api

import com.melongame.playlistmaker.media.domain.models.Playlist
import com.melongame.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistViewInteractor {
    fun getPlaylistById(playlistId: Long): Flow<Playlist?>
    fun getTracksForPlaylist(playlistId: Long): Flow<List<Track>>
    suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long)
}