package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.ITunesTrack

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    fun addTrackToHistory(track: ITunesTrack)

    fun getHistory(): ArrayList<ITunesTrack>

    fun clearHistory()

    interface TracksConsumer {
        fun consume(foundTracks: List<ITunesTrack>?, errorMessage: String?)
    }
}