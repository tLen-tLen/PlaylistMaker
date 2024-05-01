package com.example.playlistmaker.settings.domain.api

interface SettingsInteractor {
    fun isDarkTheme(): Boolean

    fun updateThemeSettings(isChecked: Boolean)
}