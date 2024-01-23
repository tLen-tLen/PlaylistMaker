package com.example.playlistmaker.dataclasses

data class ITunesTrackResponse(
    val resultCount: Int,
    val results: List<ITunesTrack>
)
