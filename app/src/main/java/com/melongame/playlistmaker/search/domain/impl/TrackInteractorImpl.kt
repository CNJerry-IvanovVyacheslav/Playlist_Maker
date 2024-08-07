package com.melongame.playlistmaker.search.domain.impl

import com.melongame.playlistmaker.search.domain.api.SearchHistoryRepository
import com.melongame.playlistmaker.search.domain.api.TrackInteractor
import com.melongame.playlistmaker.search.domain.api.TrackRepository
import com.melongame.playlistmaker.search.domain.models.Track
import com.melongame.playlistmaker.util.SearchResult
import java.util.concurrent.Executors

class TrackInteractorImpl(
    private val trackRepository: TrackRepository,
    private val historyRepository: SearchHistoryRepository,
) : TrackInteractor {

    private val executor = Executors.newCachedThreadPool()
    override fun searchTrack(expression: String, consumer: (SearchResult<List<Track>>) -> Unit) {
        executor.execute {
            consumer(trackRepository.searchTrack(expression))
        }
    }

    override fun saveTrack(track: Track) {
        historyRepository.saveTrack(track)
    }

    override fun getSearchHistory(): List<Track> {
        return historyRepository.getSearchHistory()
    }

    override fun clearSearchHistory() {
        historyRepository.clearSearchHistory()
    }
}