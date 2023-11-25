package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSearchBtnListeners()
        setLibraryBtnListeners()
        setSettingsBtnListeners()
    }

    private fun setSearchBtnListeners() {
        val searchBtn = findViewById<MaterialButton>(R.id.search_btn)

        searchBtn.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setLibraryBtnListeners() {
        val libraryBtn = findViewById<MaterialButton>(R.id.library_btn)

        libraryBtn.setOnClickListener {
            val intent = Intent(this, LibraryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setSettingsBtnListeners() {
        val settingsBtn = findViewById<MaterialButton>(R.id.settings_btn)

        settingsBtn.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}