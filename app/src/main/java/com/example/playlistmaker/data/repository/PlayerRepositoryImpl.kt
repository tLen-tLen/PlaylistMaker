package com.example.playlistmaker.data.repository

import android.media.MediaPlayer
import com.example.playlistmaker.domain.enums.PlayerStatus
import com.example.playlistmaker.domain.models.ITunesTrack
import com.example.playlistmaker.domain.repository.PlayerRepository


class PlayerRepositoryImpl: PlayerRepository {

    private val mediaPlayer = MediaPlayer()

    override var playerState = PlayerStatus.STATE_DEFAULT

    private lateinit var afterStart: () -> Unit
    private lateinit var afterPause: () -> Unit


     override fun preparePlayer(track: ITunesTrack) {

        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerStatus.STATE_PREPARED
        }

    }

     override fun setOnCompletionListener(onCompletionListener: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerStatus.STATE_PREPARED
            onCompletionListener()
        }
    }

    override fun setAfterStartListener(afterStartListener: () -> Unit) {
        afterStart = afterStartListener
    }

    override fun setAfterPauseListener(afterPauseListener: () -> Unit) {
        afterPause = afterPauseListener
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerStatus.STATE_PLAYING

        afterStart()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PlayerStatus.STATE_PAUSED

        afterPause()
    }

    override fun playbackControl() {
        when(playerState) {
            PlayerStatus.STATE_PLAYING -> {
                pausePlayer()
            }
            PlayerStatus.STATE_PREPARED, PlayerStatus.STATE_PAUSED -> {
                startPlayer()
            }
            else -> {}
        }
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }
}