package com.example.playlistmaker.di

import android.content.Context
import com.example.playlistmaker.settings.domain.api.ExternalNavigator
import com.example.playlistmaker.settings.domain.impl.ExternalNavigatorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    single {
        androidContext().getSharedPreferences("shared_preference", Context.MODE_PRIVATE)
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }
}