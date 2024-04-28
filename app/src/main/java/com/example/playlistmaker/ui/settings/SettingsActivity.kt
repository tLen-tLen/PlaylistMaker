package com.example.playlistmaker.ui.settings

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.ui.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setCurrentTheme()
        setBackBtnListener()
        setShareBtnListener()
        setSupportBtnListener()
        setTermsOfUseBtnListener()
        setThemeSwitcherListener()
    }

    private fun setCurrentTheme() {
        when (this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.themeSwitcher.isChecked = true
            }
            Configuration.UI_MODE_NIGHT_NO -> {}
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {}
        }
    }

    private fun setThemeSwitcherListener() {
        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
        }
    }

    private fun setBackBtnListener() {
        binding.back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setShareBtnListener() {
        val message = "https://practicum.yandex.ru/android-developer/"

        binding.share.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, message)
            intent.type = "text/plain"
            val intentChooser = Intent.createChooser(intent, getString(R.string.share_app))
            startActivity(intentChooser)
        }
    }

    private fun setSupportBtnListener() {
        binding.support.setOnClickListener {
            val email = "lastonetwo18@gmail.com"
            val uri = Uri.parse("mailto:$email?subject=${getString(R.string.support_subject)}&body=${getString(
                R.string.support_message_default
            )}")

            val intent = Intent(Intent.ACTION_SENDTO, uri)
            startActivity(Intent.createChooser(intent, getString(R.string.write_support)))
        }
    }

    private fun setTermsOfUseBtnListener() {
        binding.termsOfUse.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://yandex.ru/legal/practicum_offer/"))
            startActivity(intent)
        }
    }
}