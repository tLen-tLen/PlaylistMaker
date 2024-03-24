package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.ITunesTrack

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)
    interface TracksConsumer {
        fun consume(foundTracks: List<ITunesTrack>)
    }
}