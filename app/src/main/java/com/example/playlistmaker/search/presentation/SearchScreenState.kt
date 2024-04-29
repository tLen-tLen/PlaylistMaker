package com.example.playlistmaker.search.presentation

import com.example.playlistmaker.domain.models.ITunesTrack

sealed interface SearchScreenState {
    object Loading : SearchScreenState
    data class SearchContent(val tracks: List<ITunesTrack>) : SearchScreenState
    data class HistoryContent(val tracks: List<ITunesTrack>) : SearchScreenState
    data class Error(val errorMessage: String) : SearchScreenState
    object EmptySearch : SearchScreenState
    object EmptyScreen : SearchScreenState
}