package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.ITunesTrack

interface SearchHistory {
    fun read(): ArrayList<ITunesTrack>
    fun write(addedTrack: ITunesTrack? = null)
    fun clear()
}