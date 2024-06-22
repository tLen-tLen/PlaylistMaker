package com.example.playlistmaker.settings.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.api.SettingsInteractor
import com.example.playlistmaker.settings.domain.api.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
): ViewModel() {

    private val stateLiveData = MutableLiveData<Boolean>()

    fun observeState(): LiveData<Boolean> = stateLiveData

    init {
        stateLiveData.value = settingsInteractor.isDarkTheme()
    }

    fun onSharePressed() {
        sharingInteractor.shareApp()
    }

    fun sendMainToSupport() {
        sharingInteractor.openSupport()
    }

    fun openUserAgreement() {
        sharingInteractor.openTerms()
    }

    fun themeSwitch(isChecked: Boolean) {
        stateLiveData.postValue(isChecked)
        settingsInteractor.updateThemeSettings(isChecked)
    }
}