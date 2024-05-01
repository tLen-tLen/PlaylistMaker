package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.search.domain.models.ITunesTrack
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.utils.Resource
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            when (val resource = repository.searchTracks(expression)) {
                is Resource.Success -> {
                    consumer.consume(resource.data, null)
                }

                is Resource.Error -> {
                    consumer.consume(null, resource.message)
                }
            }
        }
    }

    override fun addTrackToHistory(track: ITunesTrack) {
        repository.addTrackToHistory(track)
    }

    override fun getHistory(): ArrayList<ITunesTrack> {
        return repository.readSearchHistory()
    }

    override fun clearHistory() {
        repository.clearHistory()
    }
}