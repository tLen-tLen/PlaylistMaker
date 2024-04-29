package com.example.playlistmaker.search.data

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.ITunesTrackResponse
import com.example.playlistmaker.data.dto.ITunesTrackSearchRequest
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.ITunesTrack
import com.example.playlistmaker.utils.Resource

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val localStorage: SearchHistory
) : TracksRepository {

    override fun searchTracks(expression: String): Resource<List<ITunesTrack>> {
        val response = networkClient.doRequest(ITunesTrackSearchRequest(expression))

        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }

            200 -> {
                Resource.Success((response as ITunesTrackResponse).results.map {
                    ITunesTrack(
                        it.trackId,
                        it.trackName,
                        it.artistName,
                        it.trackTimeMillis,
                        it.artworkUrl100,
                        it.country,
                        it.collectionName,
                        it.primaryGenreName,
                        it.releaseDate,
                        it.previewUrl
                    )
                })
            }

            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }

    override fun readSearchHistory(): ArrayList<ITunesTrack> {
        return localStorage.read()
    }

    override fun addTrackToHistory(track: ITunesTrack) {
        localStorage.write(track)
    }

    override fun clearHistory() {
        localStorage.clear()
    }
}