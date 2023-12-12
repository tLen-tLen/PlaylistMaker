package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchString = findViewById<EditText>(R.id.search_string)
        val clearButton = findViewById<ImageView>(R.id.clear_search_btn)

        setBackBtnListener()
        setClearBtnListener(searchString, clearButton)
        setSearchWatcher(searchString, clearButton)
    }

    private fun setClearBtnListener(searchString: EditText, clearButton: ImageView) {
        clearButton.setOnClickListener {
            searchString.setText("")
        }
    }

    private fun setSearchWatcher(searchString: EditText, clearButton: ImageView) {
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
            }
            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        searchString.addTextChangedListener(watcher)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun setBackBtnListener() {
        val backBtn = findViewById<ImageView>(R.id.back)

        backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}