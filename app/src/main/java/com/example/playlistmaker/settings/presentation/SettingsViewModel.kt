package com.example.playlistmaker.settings.presentation

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.Creator
import com.example.playlistmaker.settings.domain.api.SettingsInteractor
import com.example.playlistmaker.settings.domain.api.SharingInteractor
import com.example.playlistmaker.ui.App

class SettingsViewModel(
    private val application: App,
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
): AndroidViewModel(application) {

    private val stateLiveData = MutableLiveData<Boolean>()

    fun observeState(): LiveData<Boolean> = stateLiveData

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as App
                val settingsInteractor = Creator.provideSettingsInteractor(application.applicationContext)
                val sharingInteractor = Creator.provideSharingInteractor(application.applicationContext)
                SettingsViewModel(application, sharingInteractor, settingsInteractor)
            }
        }
    }

    fun onSharePressed() {
        sharingInteractor.shareApp()
    }

    fun onSupportPressed() {
        sharingInteractor.openSupport()
    }

    fun onUserAgreementPressed() {
        sharingInteractor.openTerms()
    }

    fun onThemeSwitchedPressed(isChecked: Boolean) {
        application.switchTheme(isChecked)
        stateLiveData.postValue(isChecked)
        settingsInteractor.updateThemeSettings(isChecked)
    }

    fun getDarkThemeIsEnabled(): Boolean {
        return settingsInteractor.isDarkTheme()
    }
}