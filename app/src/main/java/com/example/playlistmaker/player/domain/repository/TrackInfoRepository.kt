package com.example.playlistmaker.player.domain.repository

import com.example.playlistmaker.search.domain.models.ITunesTrack

interface TrackInfoRepository {
    fun getTrack(): ITunesTrack
}
