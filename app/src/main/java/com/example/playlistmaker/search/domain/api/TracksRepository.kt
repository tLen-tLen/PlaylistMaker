package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.domain.models.ITunesTrack
import com.example.playlistmaker.utils.Resource

interface TracksRepository {
    fun searchTracks(expression: String) : Resource<List<ITunesTrack>>
    fun readSearchHistory(): ArrayList<ITunesTrack>
    fun addTrackToHistory(track: ITunesTrack)
    fun clearHistory()
}