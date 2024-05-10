package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import com.example.playlistmaker.search.data.TracksRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.player.data.repository.PlayerRepositoryImpl
import com.example.playlistmaker.player.data.repository.TrackInfoRepositoryImpl
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.player.domain.repository.PlayerRepository
import com.example.playlistmaker.player.domain.usecases.GetTrackUseCase
import com.example.playlistmaker.search.data.SearchHistoryImpl
import com.example.playlistmaker.settings.domain.impl.ExternalNavigatorImpl
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.settings.domain.impl.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.impl.SharingInteractorImpl
import com.example.playlistmaker.settings.domain.api.ExternalNavigator
import com.example.playlistmaker.settings.domain.api.SettingsInteractor
import com.example.playlistmaker.settings.domain.api.SettingsRepository
import com.example.playlistmaker.settings.domain.api.SharingInteractor
import com.example.playlistmaker.player.domain.repository.TrackInfoRepository

object Creator {
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
        return PlayerRepositoryImpl(createMediaPlayer())
    }

    private fun createMediaPlayer(): MediaPlayer {
        return MediaPlayer()
    }
}