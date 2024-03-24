package com.example.playlistmaker.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    private var darkTheme = false

    private val sharedPrefs by lazy {
        getSharedPreferences(SHARED_PREFERENCE_NAME, MODE_PRIVATE)
    }

    override fun onCreate() {
        super.onCreate()
        darkTheme = sharedPrefs.getBoolean(THIEME_SWITCH_KEY, false)
        sharedPrefs.edit()
            .putBoolean(THIEME_SWITCH_KEY, darkTheme)
            .apply()
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        private const val THIEME_SWITCH_KEY = "THIEME_SWITCH_KEY"
        private const val SHARED_PREFERENCE_NAME = "PLAYLISTMAKER_SHARED_PREFS"
    }
}