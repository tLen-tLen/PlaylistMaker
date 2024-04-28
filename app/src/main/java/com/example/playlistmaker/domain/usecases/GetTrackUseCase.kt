package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.models.ITunesTrack
import com.example.vehicle_shop_clean.domain.repository.TrackInfoRepository

class GetTrackUseCase(
    private val repository: TrackInfoRepository
) {
    fun execute(): ITunesTrack {
        return repository.getTrack()
    }
}