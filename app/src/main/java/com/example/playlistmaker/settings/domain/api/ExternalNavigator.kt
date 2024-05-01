package com.example.playlistmaker.settings.domain.api

import com.example.playlistmaker.settings.domain.model.EmailData

interface ExternalNavigator {
    fun shareLink(message: String)

    fun openLink(termsLink: String)

    fun openEmail(supportEmailData: EmailData)
}