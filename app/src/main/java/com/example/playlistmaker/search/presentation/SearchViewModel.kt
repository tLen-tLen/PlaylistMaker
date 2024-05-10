package com.example.playlistmaker.search.presentation

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.models.ITunesTrack
import com.example.playlistmaker.search.domain.api.TracksInteractor

class SearchViewModel(
    private val interactor: TracksInteractor
): ViewModel() {
    private val stateLiveData = MutableLiveData<SearchScreenState>()

    fun observeState(): LiveData<SearchScreenState> = stateLiveData

    private var lastSearchText: String = ""

    private val handler = Handler(Looper.getMainLooper())

    fun searchDebounce(changedText: String?) {
        if (changedText!!.isEmpty()) {
            val historyTrackList = interactor.getHistory()
            renderState(SearchScreenState.HistoryContent(historyTrackList))
        } else {
            renderState(SearchScreenState.Loading)
        }
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        lastSearchText = changedText
    }

    fun renderState(state: SearchScreenState) {
        stateLiveData.postValue(state)
    }

    private val searchRunnable = Runnable {
        if (lastSearchText.isNotEmpty()) {
            search(lastSearchText)
        }
    }

    fun search(search: String) {
        if (search.isNotEmpty()) {
            renderState(SearchScreenState.Loading)

            interactor.searchTracks(search, object : TracksInteractor.TracksConsumer {
                override fun consume(foundTracks: List<ITunesTrack>?, errorMessage: String?) {
                    val tracks = mutableListOf<ITunesTrack>()
                    if (foundTracks != null) {
                        tracks.addAll(foundTracks)
                        when {
                            tracks.isEmpty() -> {
                                renderState(SearchScreenState.EmptySearch)
                            }

                            else -> {
                                renderState(SearchScreenState.SearchContent(tracks))
                            }
                        }
                    } else {
                        if (errorMessage !== null) {
                            renderState(SearchScreenState.Error(errorMessage))
                        } else {
                            renderState(SearchScreenState.Error("Произошла ошибка"))
                        }

                    }
                }
            })
        }
    }

    fun removeCallbacks() {
        handler.removeCallbacks(searchRunnable)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}