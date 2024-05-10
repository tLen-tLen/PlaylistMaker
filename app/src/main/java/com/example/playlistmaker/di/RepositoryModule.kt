package com.example.playlistmaker.di

import com.example.playlistmaker.settings.domain.api.SettingsRepository
import com.example.playlistmaker.settings.domain.impl.SettingsRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }
}