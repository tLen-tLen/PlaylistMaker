package com.example.playlistmaker.settings.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]
        viewModel.observeState().observe(this) {
            render(it)
        }

        setCurrentTheme(viewModel)
        setBackBtnListener()
        setShareBtnListener(viewModel)
        setSupportBtnListener(viewModel)
        setTermsOfUseBtnListener(viewModel)
        setThemeSwitcherListener(viewModel)
    }

    private fun render(isDarkTheme: Boolean) {
        binding.themeSwitcher.isChecked = isDarkTheme
    }

    private fun setCurrentTheme(viewModel: SettingsViewModel) {
        if (viewModel.getDarkThemeIsEnabled()) {
            binding.themeSwitcher.isChecked = true
        }
    }

    private fun setThemeSwitcherListener(viewModel: SettingsViewModel) {
        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.onThemeSwitchedPressed(checked)
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
            viewModel.onSupportPressed()
        }
    }

    // todo убрать метод
    private fun setTermsOfUseBtnListener(viewModel: SettingsViewModel) {
        binding.termsOfUse.setOnClickListener {
            viewModel.onUserAgreementPressed()
        }
    }
}