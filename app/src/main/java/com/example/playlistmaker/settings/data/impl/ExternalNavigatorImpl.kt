package com.example.playlistmaker.settings.data.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.settings.domain.api.ExternalNavigator
import com.example.playlistmaker.settings.domain.model.EmailData

class ExternalNavigatorImpl(private val context: Context): ExternalNavigator {
    override fun shareLink(message: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, message)
        intent.type = "text/plain"
        val intentChooser = Intent.createChooser(intent, context.getString(R.string.share_app))
        intentChooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intentChooser)
    }

    override fun openLink(termsLink: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(termsLink))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun openEmail(supportEmailData: EmailData) {
        val uri = Uri.parse("mailto:${supportEmailData.email}?subject=${supportEmailData.subject}&body=${supportEmailData.textMessage}")

        val intent = Intent(Intent.ACTION_SENDTO, uri)
        context.startActivity(
            Intent.createChooser(intent, context.getString(R.string.write_support)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}