package com.example.playlistmaker.settings.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.observeState().observe(this) {
            render(it)
        }

        setBackBtnListener()
        setShareBtnListener(viewModel)
        setSupportBtnListener(viewModel)
        setTermsOfUseBtnListener(viewModel)
        setThemeSwitcherListener(viewModel)
    }

    private fun render(isDarkTheme: Boolean) {
        binding.themeSwitcher.isChecked = isDarkTheme
    }

    private fun setThemeSwitcherListener(viewModel: SettingsViewModel) {
        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.themeSwitch(checked)
        }
    }

    private fun setBackBtnListener() {
        binding.back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setShareBtnListener(viewModel: SettingsViewModel) {
        binding.share.setOnClickListener {
            viewModel.onSharePressed()
        }
    }

    private fun setSupportBtnListener(viewModel: SettingsViewModel) {
        binding.support.setOnClickListener {
            viewModel.sendMainToSupport()
        }
    }

    private fun setTermsOfUseBtnListener(viewModel: SettingsViewModel) {
        binding.termsOfUse.setOnClickListener {
            viewModel.openUserAgreement()
        }
    }
}