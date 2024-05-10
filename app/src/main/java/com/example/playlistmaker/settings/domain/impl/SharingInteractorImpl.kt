package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.settings.domain.api.ExternalNavigator
import com.example.playlistmaker.settings.domain.api.SharingInteractor
import com.example.playlistmaker.settings.domain.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator
): SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return SHARE_LINK
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(EMAIL, SUBJECT, TEXT_MESSAGE)
    }

    private fun getTermsLink(): String {
        return TERMS_LINK
    }

    companion object {
        private const val SHARE_LINK = "https://practicum.yandex.ru/android-developer/";
        private const val EMAIL = "lastonetwo18@gmail.com"
        private const val SUBJECT = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
        private const val TEXT_MESSAGE = "Спасибо разработчикам и разработчицам за крутое приложение!"
        private const val TERMS_LINK = "https://yandex.ru/legal/practicum_offer/"
    }
}