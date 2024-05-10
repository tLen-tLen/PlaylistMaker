package com.example.playlistmaker.player.presentation

sealed interface PlayerScreenState {
    object DefaultScreenState: PlayerScreenState
    object PreparedScreenState : PlayerScreenState
    object PlayingScreenState: PlayerScreenState
    object PauseScreenState: PlayerScreenState
    data class TimerState(val playbackTimer: String) : PlayerScreenState
}