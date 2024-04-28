package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.ITunesTrackResponse
import com.example.playlistmaker.data.dto.ITunesTrackSearchRequest
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.ITunesTrack

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun searchTracks(expression: String): List<ITunesTrack> {
        val response = networkClient.doRequest(ITunesTrackSearchRequest(expression))
        if (response.resultCode == 200) {
            return (response as ITunesTrackResponse).results.map {
                ITunesTrack(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl
                )
            }
        } else {
            return emptyList()
        }
    }
}