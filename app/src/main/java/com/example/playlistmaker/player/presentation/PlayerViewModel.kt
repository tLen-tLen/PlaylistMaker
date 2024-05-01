package com.example.playlistmaker.player.presentation

import android.app.Application
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.Creator
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.search.domain.models.ITunesTrack
import com.example.playlistmaker.utils.converters.DateTimeConverter

class PlayerViewModel(
    private val track: ITunesTrack,
    private val playerInteractor: PlayerInteractor,
    application: Application
) : AndroidViewModel(application) {

    companion object {
        fun getViewModelFactory(intent: Intent): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val trackUseCase = Creator.provideGetTrackUseCase(intent)
                val track = trackUseCase.execute()
                val interactor = Creator.provideGetPlayerInteractor()
                PlayerViewModel(track, interactor, this[APPLICATION_KEY] as Application)
            }
        }

        private const val PLAY_TIME_DELAY = 300L
    }

    private val handler = Handler(Looper.getMainLooper())

    private val stateLiveData = MutableLiveData<PlayerScreenState>()

    fun observeState(): LiveData<PlayerScreenState> = stateLiveData

    private fun renderState(playerState: PlayerScreenState) {
        stateLiveData.postValue(playerState)
    }

    fun playbackControl() {
        if (playerInteractor.isPlaying()) {
            pausePlayer()
        } else {
            startPlayer()
        }
    }

    private fun startPlayer() {
        playerInteractor.startPlayer()
        renderState(PlayerScreenState.PlayingScreenState)
        handler.post(this::updatePlayTime)
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer()
        renderState(PlayerScreenState.PauseScreenState)
        handler.removeCallbacks(this::updatePlayTime)
    }

    fun releasePlayer() {
        playerInteractor.releasePlayer()
        handler.removeCallbacks(this::updatePlayTime)
    }

    fun preparePlayer() {
        playerInteractor.preparePlayer(
            url = track.previewUrl ?: "",
            onPreparedListener = {
                renderState(PlayerScreenState.PreparedScreenState)
            },
            onCompletionListener = {
                handler.removeCallbacks(this::updatePlayTime)
                renderState(PlayerScreenState.PreparedScreenState)
            }
        )
    }

    private fun updatePlayTime() {
        handler.post(object : Runnable {
            override fun run() {
                handler.postDelayed({
                    if (playerInteractor.isPlaying()) {
                        val playbackTime = DateTimeConverter.millisToMmSs(playerInteractor.getCurrentPosition())
                        stateLiveData.postValue(PlayerScreenState.TimerState(playbackTime))
                        handler.postDelayed(this, PLAY_TIME_DELAY)
                    }
                }, PLAY_TIME_DELAY)
            }
        })
    }

    fun getTrack(): ITunesTrack {
        return track
    }
}