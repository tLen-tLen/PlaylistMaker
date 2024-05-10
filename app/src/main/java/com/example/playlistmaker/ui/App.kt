package com.example.playlistmaker.ui

import android.app.Application
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.settings.domain.api.SettingsInteractor
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.android.ext.android.inject

class App : Application() {

    private val settingsInteractor: SettingsInteractor by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                dataModule,
                repositoryModule,
                interactorModule,
                viewModelModule
            )
        }
        settingsInteractor.updateThemeSettings(settingsInteractor.isDarkTheme())
    }
}