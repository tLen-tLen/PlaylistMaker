package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.ITunesTrack

interface TracksRepository {
    fun searchTracks(expression: String) : List<ITunesTrack>
}