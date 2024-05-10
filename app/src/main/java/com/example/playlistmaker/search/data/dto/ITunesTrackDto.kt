package com.example.playlistmaker.search.data.dto

data class ITunesTrackDto(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val country: String,
    val collectionName: String,
    val primaryGenreName: String,
    val releaseDate: String,
    val previewUrl: String?
)