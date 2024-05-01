package com.example.vehicle_shop_clean.domain.repository

import com.example.playlistmaker.search.domain.models.ITunesTrack

interface TrackInfoRepository {
    fun getTrack(): ITunesTrack
}
