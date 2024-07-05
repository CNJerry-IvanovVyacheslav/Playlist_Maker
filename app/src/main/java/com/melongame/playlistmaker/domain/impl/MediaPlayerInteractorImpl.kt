package com.melongame.playlistmaker.domain.impl

import com.melongame.playlistmaker.domain.api.MediaPlayerInteractor
import com.melongame.playlistmaker.domain.api.MediaPlayerRepository

class MediaPlayerInteractorImpl(private val repository: MediaPlayerRepository) : MediaPlayerInteractor {

    override fun setDataSource(url: String) {
        repository.setDataSource(url)
    }

    override fun prepareAsync() {
        repository.prepareAsync()
    }

    override fun start() {
        repository.start()
    }

    override fun pause() {
        repository.pause()
    }

    override fun seekTo(position: Int) {
        repository.seekTo(position)
    }

    override fun getCurrentPosition(): Int {
        return repository.getCurrentPosition()
    }

    override fun isPlaying(): Boolean {
        return repository.isPlaying()
    }

    override fun release() {
        repository.release()
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        repository.setOnCompletionListener {listener()}
    }

}