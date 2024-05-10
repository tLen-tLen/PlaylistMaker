package com.example.playlistmaker.settings.domain.impl

import android.content.SharedPreferences
import com.example.playlistmaker.settings.domain.api.SettingsRepository

class SettingsRepositoryImpl(
    private val sharedPreferences: SharedPreferences
) : SettingsRepository {

    override fun isDarkTheme(): Boolean {
        return sharedPreferences.getBoolean(DARK_THEME_ENABLED,false)
    }

    override fun updateThemeSettings(isChecked: Boolean) {
        sharedPreferences.edit()
            .putBoolean(DARK_THEME_ENABLED, isChecked)
            .apply()
    }

    companion object{
        const val DARK_THEME_ENABLED = "DARK_THEME_ENABLED"
    }
}