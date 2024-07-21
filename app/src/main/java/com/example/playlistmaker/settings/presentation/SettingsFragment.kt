package com.example.playlistmaker.settings.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)

        binding = FragmentSettingsBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

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