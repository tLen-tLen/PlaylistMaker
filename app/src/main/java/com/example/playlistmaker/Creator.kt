package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import com.example.playlistmaker.search.data.TracksRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.player.data.repository.PlayerRepositoryImpl
import com.example.playlistmaker.player.data.repository.TrackInfoRepositoryImpl
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.player.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.player.domain.repository.PlayerRepository
import com.example.playlistmaker.player.domain.usecases.GetTrackUseCase
import com.example.playlistmaker.search.data.SearchHistory
import com.example.playlistmaker.settings.data.impl.ExternalNavigatorImpl
import com.example.playlistmaker.settings.data.impl.SettingsInteractorImpl
import com.example.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.example.playlistmaker.settings.data.impl.SharingInteractorImpl
import com.example.playlistmaker.settings.domain.api.ExternalNavigator
import com.example.playlistmaker.settings.domain.api.SettingsInteractor
import com.example.playlistmaker.settings.domain.api.SettingsRepository
import com.example.playlistmaker.settings.domain.api.SharingInteractor
import com.example.playlistmaker.player.domain.repository.TrackInfoRepository

object Creator {
    private const val SHARED_PREFERENCE = "SHARED_PREFERENCE"

    private fun getTracksRepository(context: Context): TracksRepository {
        return TracksRepositoryImpl(
            RetrofitNetworkClient(),
            SearchHistory(context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE))
        )
    }

    fun provideTracksInteractor(context: Context): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository(context))
    }

    fun provideGetTrackUseCase(intent: Intent): GetTrackUseCase {
        return GetTrackUseCase(getTrackInfoRepository(intent))
    }

    private fun getTrackInfoRepository(intent: Intent): TrackInfoRepository {
        return TrackInfoRepositoryImpl(intent)
    }

    fun provideGetPlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(providePlayerRepository())
    }

    private fun providePlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }

    private fun getSettingsRepository(context: Context) : SettingsRepository {
        return SettingsRepositoryImpl(context.getSharedPreferences(SHARED_PREFERENCE,Context.MODE_PRIVATE))
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator(context))
    }

    private fun getExternalNavigator(context: Context) : ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }
}