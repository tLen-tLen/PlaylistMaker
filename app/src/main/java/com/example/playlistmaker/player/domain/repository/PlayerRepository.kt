package com.example.playlistmaker.player.domain.repository

interface PlayerRepository {

    fun preparePlayer(url: String, onCompletionListener: () -> Unit, onPreparedListener: () -> Unit)

    fun startPlayer()

    fun pausePlayer()

    fun releasePlayer()

    fun getCurrentPosition(): Int

    fun isPlaying(): Boolean
}