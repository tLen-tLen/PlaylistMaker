package com.example.playlistmaker.player.data.repository

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.repository.PlayerRepository


class PlayerRepositoryImpl() : PlayerRepository {

    private val mediaPlayer = MediaPlayer()

    override fun preparePlayer(url: String, onCompletionListener: () -> Unit, onPreparedListener: () -> Unit) {
        mediaPlayer.apply {
            setDataSource(url)
            mediaPlayer.prepareAsync()
            setOnPreparedListener {
                onPreparedListener.invoke()
            }
            setOnCompletionListener {
                onCompletionListener.invoke()
            }
        }
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }
}