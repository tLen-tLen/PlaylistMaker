package com.example.playlistmaker.library.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator

class LibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLibraryBinding

    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.libraryViewPager.adapter = MediaLibraryAdapter(supportFragmentManager, lifecycle)
        binding.settingsToolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.libraryViewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.favourite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()
    }


    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}