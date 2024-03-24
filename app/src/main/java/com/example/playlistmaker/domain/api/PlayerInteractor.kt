package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.enums.PlayerStatus
import com.example.playlistmaker.domain.models.ITunesTrack

interface PlayerInteractor {
    fun preparePlayer(track: ITunesTrack)

    fun startPlayer()

    fun pausePlayer()

    fun playbackControl()

    fun releasePlayer()

    fun setOnCompletionListener(onCompletionListener: () -> Unit)

    fun setAfterStartListener(afterStartListener: () -> Unit)

    fun setAfterPauseListener(afterPauseListener: () -> Unit)

    fun getPlayerStatus(): PlayerStatus

    fun getCurrentPosition(): Int
}