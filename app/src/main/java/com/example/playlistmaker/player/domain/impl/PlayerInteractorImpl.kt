package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.repository.PlayerRepository

class PlayerInteractorImpl (private val repository: PlayerRepository): PlayerInteractor {

    override fun preparePlayer(
        url: String,
        onCompletionListener: () -> Unit,
        onPreparedListener: () -> Unit
    ) {
        repository.preparePlayer(url, onCompletionListener, onPreparedListener)
    }

    override fun startPlayer() {
        repository.startPlayer()
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }

    override fun releasePlayer() {
        repository.releasePlayer()
    }

    override fun getCurrentPosition(): Int {
        return repository.getCurrentPosition()
    }

    override fun isPlaying(): Boolean {
        return repository.isPlaying()
    }
}