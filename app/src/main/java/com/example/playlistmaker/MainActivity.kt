package com.example.playlistmaker

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
        val listener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Происходит поиск", Toast.LENGTH_SHORT).show()
            }
        }

        searchBtn.setOnClickListener(listener)
    }
    private fun setLibraryBtnListeners() {
        val libraryBtn = findViewById<MaterialButton>(R.id.library_btn)

        libraryBtn.setOnClickListener {
            Toast.makeText(this@MainActivity, "Загружается медиатека", Toast.LENGTH_SHORT).show()
        }
    }
    private fun setSettingsBtnListeners() {
        val settingsBtn = findViewById<MaterialButton>(R.id.settings_btn)

        settingsBtn.setOnClickListener {
            Toast.makeText(this@MainActivity, "Настройки настроены", Toast.LENGTH_SHORT).show()
        }
    }
}