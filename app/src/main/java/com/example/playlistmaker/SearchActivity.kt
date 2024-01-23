package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.adapters.TrackListAdapter
import com.example.playlistmaker.api.ITunes.ITunesApi
import com.example.playlistmaker.dataclasses.ITunesTrack
import com.example.playlistmaker.dataclasses.ITunesTrackResponse
import com.example.playlistmaker.enums.TrackListStatus
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    companion object {
        private const val SEARCH_STRING_KEY = "search_string"
    }

    private var searchSavedData: String = ""

    private val trackDataList:MutableList<ITunesTrack> = mutableListOf()

    private lateinit var trackListRV: RecyclerView
    private lateinit var updateBtn: MaterialButton
    private lateinit var errorImage: ImageView
    private lateinit var errorTitle: TextView

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ITunesApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchString = findViewById<EditText>(R.id.search_string)
        val clearButton = findViewById<ImageView>(R.id.clear_search_btn)
        trackListRV = findViewById(R.id.tracks)
        updateBtn = findViewById(R.id.update_btn)
        errorImage = findViewById(R.id.error_image)
        errorTitle = findViewById(R.id.error_title)

        setBackBtnListener()
        setClearBtnListener(searchString, clearButton)
        setSearchWatcher(searchString, clearButton)

        searchString.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search(searchString)
                true
            }
            false
        }
        updateBtn.setOnClickListener {
            search(searchString)
        }

        initTrackList()
    }

    private fun search(searchString: EditText) {
        val search = searchString.text.toString()

        if (search.isNotEmpty()) {
            itunesService.search(search).enqueue(object : Callback<ITunesTrackResponse> {

                override fun onResponse(call: Call<ITunesTrackResponse>, response: Response<ITunesTrackResponse>) {
                    if (response.code() == 200) {
                        trackDataList.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            trackDataList.addAll(response.body()!!.results)
                            initTrackList()
                            setTrackListStatus(TrackListStatus.SUCCESS)
                        } else {
                            setTrackListStatus(TrackListStatus.NOT_FOUND)
                        }
                    } else {
                        setTrackListStatus(TrackListStatus.FAIL)
                    }
                }

                override fun onFailure(call: Call<ITunesTrackResponse>, t: Throwable) {
                    setTrackListStatus(TrackListStatus.FAIL)
                }
            })
        }
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
            trackDataList.clear()
            initTrackList()
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

    private fun initTrackList() {
        trackListRV.layoutManager = LinearLayoutManager(this)
        trackListRV.adapter = TrackListAdapter(trackDataList)
    }

    private fun setTrackListStatus(status: TrackListStatus) {
        when (status) {
            TrackListStatus.SUCCESS -> {
                trackListRV.visibility = View.VISIBLE
                errorImage.visibility = View.GONE
                errorTitle.visibility = View.GONE
                updateBtn.visibility = View.GONE
            }
            TrackListStatus.NOT_FOUND -> {
                trackListRV.visibility = View.GONE
                errorImage.setImageResource(R.drawable.not_found)
                errorTitle.setText(R.string.not_found)
                errorImage.visibility = View.VISIBLE
                errorTitle.visibility = View.VISIBLE
            }
            else -> {
                trackListRV.visibility = View.GONE
                errorImage.setImageResource(R.drawable.network_error)
                errorTitle.setText(R.string.network_error)

                errorImage.visibility = View.VISIBLE
                errorTitle.visibility = View.VISIBLE
                updateBtn.visibility = View.VISIBLE
            }
        }

    }
}