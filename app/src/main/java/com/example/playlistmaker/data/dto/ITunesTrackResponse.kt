package com.example.playlistmaker.data.dto

import com.example.playlistmaker.domain.models.ITunesTrack

data class ITunesTrackResponse(
    val resultCount: Int,
    val results: List<ITunesTrack>
): Response()
