package com.example.playlistmaker.settings.data.impl

import com.example.playlistmaker.settings.domain.api.SettingsInteractor
import com.example.playlistmaker.settings.domain.api.SettingsRepository

class SettingsInteractorImpl (
    private val settingsRepository: SettingsRepository
) : SettingsInteractor{
    override fun isDarkTheme(): Boolean {
        return settingsRepository.isDarkTheme()
    }

    override fun updateThemeSettings(isChecked: Boolean) {
        settingsRepository.updateThemeSettings(isChecked)
    }
}