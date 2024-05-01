package com.example.playlistmaker.search.domain.models

import java.io.Serializable

data class ITunesTrack(
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
) : Serializable