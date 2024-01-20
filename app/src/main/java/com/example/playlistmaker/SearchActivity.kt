package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView

class SearchActivity : AppCompatActivity() {
    companion object {
        private const val SEARCH_STRING_KEY = "search_string"
    }

    private var searchSavedData: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchString = findViewById<EditText>(R.id.search_string)
        val clearButton = findViewById<ImageView>(R.id.clear_search_btn)

        setBackBtnListener()
        setClearBtnListener(searchString, clearButton)
        setSearchWatcher(searchString, clearButton)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val searchSavedData = findViewById<EditText>(R.id.search_string).text.toString()
        outState.putString(SEARCH_STRING_KEY, searchSavedData)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchSavedData = savedInstanceState.getString(SEARCH_STRING_KEY)?: ""
    }

    private fun setClearBtnListener(searchString: EditText, clearButton: ImageView) {
        clearButton.setOnClickListener {
            searchString.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchString?.windowToken, 0)
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