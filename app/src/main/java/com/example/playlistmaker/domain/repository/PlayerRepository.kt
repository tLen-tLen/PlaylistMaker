package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.enums.PlayerStatus
import com.example.playlistmaker.search.domain.models.ITunesTrack

interface PlayerRepository {
    var playerState: PlayerStatus

    fun preparePlayer(track: ITunesTrack)

    fun startPlayer()

    fun pausePlayer()

    fun playbackControl()

    fun releasePlayer()

    fun setOnCompletionListener(onCompletionListener: () -> Unit)

    fun setAfterStartListener(afterStartListener: () -> Unit)

    fun setAfterPauseListener(afterPauseListener: () -> Unit)

    fun getCurrentPosition(): Int
}