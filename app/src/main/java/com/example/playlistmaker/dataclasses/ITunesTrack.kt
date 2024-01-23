package com.example.playlistmaker.dataclasses

data class ITunesTrack(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String
)
