package com.example.vehicle_shop_clean.domain.repository

import com.example.playlistmaker.domain.models.ITunesTrack

interface TrackInfoRepository {
    fun getTrack(): ITunesTrack
}
