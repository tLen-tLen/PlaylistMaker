package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.enums.PlayerStatus
import com.example.playlistmaker.search.domain.models.ITunesTrack
import com.example.playlistmaker.domain.repository.PlayerRepository

class PlayerInteractorImpl (private val repository: PlayerRepository): PlayerInteractor {
    override fun preparePlayer(track: ITunesTrack) {
        repository.preparePlayer(track)
    }

    override fun startPlayer() {
        repository.startPlayer()
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }

    override fun playbackControl() {
        repository.playbackControl()
    }

    override fun releasePlayer() {
        repository.releasePlayer()
    }

    override fun setOnCompletionListener(onCompletionListener: () -> Unit) {
        repository.setOnCompletionListener(onCompletionListener)
    }

    override fun setAfterStartListener(afterStartListener: () -> Unit) {
        repository.setAfterStartListener(afterStartListener)
    }

    override fun setAfterPauseListener(afterPauseListener: () -> Unit) {
        repository.setAfterPauseListener(afterPauseListener)
    }

    override fun getPlayerStatus(): PlayerStatus {
        return repository.playerState
    }

    override fun getCurrentPosition(): Int {
        return repository.getCurrentPosition()
    }
}