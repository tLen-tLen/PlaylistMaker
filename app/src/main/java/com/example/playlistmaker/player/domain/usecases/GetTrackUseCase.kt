package com.example.playlistmaker.player.domain.usecases

import com.example.playlistmaker.search.domain.models.ITunesTrack
import com.example.playlistmaker.player.domain.repository.TrackInfoRepository

class GetTrackUseCase(
    private val repository: TrackInfoRepository
) {
    fun execute(): ITunesTrack {
        return repository.getTrack()
    }
}