package com.example.playlistmaker.search.data.dto

import com.example.playlistmaker.search.domain.models.ITunesTrack

data class ITunesTrackResponse(
    val resultCount: Int,
    val results: List<ITunesTrack>
): Response()
