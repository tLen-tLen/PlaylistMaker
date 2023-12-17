package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setBackBtnListener()
        setShareBtnListener()
        setSupportBtnListener()
        setTermsOfUseBtnListener()
    }

    private fun setBackBtnListener() {
        val backBtn = findViewById<ImageView>(R.id.back)

        backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setShareBtnListener() {
        val message = "https://practicum.yandex.ru/android-developer/"
        val shareBtn = findViewById<TextView>(R.id.share)

        shareBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, message)
            intent.type = "text/plain"
            val intentChooser = Intent.createChooser(intent, getString(R.string.share_app))
            startActivity(intentChooser)
        }
    }

    private fun setSupportBtnListener() {
        val supportBtn = findViewById<TextView>(R.id.support)

        supportBtn.setOnClickListener {
            val email = "lastonetwo18@gmail.com"
            val uri = Uri.parse("mailto:$email?subject=${getString(R.string.support_subject)}&body=${getString(R.string.support_message_default)}")

            val intent = Intent(Intent.ACTION_SENDTO, uri)
            startActivity(Intent.createChooser(intent, getString(R.string.write_support)))
        }
    }

    private fun setTermsOfUseBtnListener() {
        val termsOfUseBtn = findViewById<TextView>(R.id.terms_of_use)

        termsOfUseBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://yandex.ru/legal/practicum_offer/"))
            startActivity(intent)
        }
    }
}