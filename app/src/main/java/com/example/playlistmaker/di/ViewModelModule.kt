package com.example.playlistmaker.di

import com.example.playlistmaker.search.presentation.SearchViewModel
import com.example.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        SettingsViewModel(get(),get())
    }
    viewModel<SearchViewModel> {
        SearchViewModel(get())
    }
}