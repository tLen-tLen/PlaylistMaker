package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
    private lateinit var adapter: TrackListAdapter

    private lateinit var searchString: EditText
    private lateinit var clearButton: ImageView
    private lateinit var trackListRV: RecyclerView
    private lateinit var updateBtn: MaterialButton
    private lateinit var errorImage: ImageView
    private lateinit var errorTitle: TextView
    private lateinit var historyTitleTV: TextView
    private lateinit var historyClearBtn: MaterialButton

    private lateinit var history: SearchHistory
    private var historyTracks = mutableListOf<ITunesTrack>()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ITunesApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val prefs =  getSharedPreferences(SearchHistory.HISTORY_SP, MODE_PRIVATE)
        history = SearchHistory(prefs)
        adapter = TrackListAdapter(trackDataList, prefs)

        searchString = findViewById(R.id.search_string)
        clearButton = findViewById(R.id.clear_search_btn)
        trackListRV = findViewById(R.id.rv_tracks)
        updateBtn = findViewById(R.id.update_btn)
        errorImage = findViewById(R.id.error_image)
        errorTitle = findViewById(R.id.error_title)
        historyTitleTV = findViewById(R.id.tv_history_title)
        historyClearBtn = findViewById(R.id.clear_history_btn)

        setBackBtnListener()
        setClearBtnListener(prefs)
        setSearchWatcher()
        setSearchActionListener()
        setUpdateBtnListener()

        initTrackList()
        initHistoryList()
    }

    /**
     * Установка слушателей на фокус строки поиска и на клик по кнопке "Очистить историю"
     */
    private fun initHistoryList() {
        historyTracks = history.read()

        searchString.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && searchString.text.isEmpty()) {
                trackDataList.addAll(historyTracks)
                adapter.notifyDataSetChanged()
                showHistoryViewItems(historyTracks)
            }
        }

        historyClearBtn.setOnClickListener {
            history.clear()
            trackDataList.clear()
            initTrackList()
            hideHistoryViewItems()
            Toast.makeText(this, "История очищена", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Выполнение и вывод поискового запроса
     */
    private fun search(searchString: EditText) {

        Toast.makeText(this, "Идет поиск", Toast.LENGTH_SHORT).show()
        hideHistoryViewItems()

        val search = searchString.text.toString()
        if (search.isNotEmpty()) {
            itunesService.search(search).enqueue(object : Callback<ITunesTrackResponse> {

                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(call: Call<ITunesTrackResponse>, response: Response<ITunesTrackResponse>) {
                    if (response.code() == 200) {
                        trackDataList.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            trackDataList.addAll(response.body()!!.results)
                            adapter.notifyDataSetChanged()
                            setTrackListStatus(TrackListStatus.SUCCESS)
                        } else {
                            setTrackListStatus(TrackListStatus.NOT_FOUND)
                        }
                    } else {
                        Log.d("TEST", "fail ${response.code()} ")
                        setTrackListStatus(TrackListStatus.FAIL)
                    }
                }

                override fun onFailure(call: Call<ITunesTrackResponse>, t: Throwable) {
                    Log.d("TEST", "fail")
                    setTrackListStatus(TrackListStatus.FAIL)
                }
            })
        }
    }

    /**
     * Скрыть визуальные элементы истории
     */
    private fun hideHistoryViewItems() {
        historyTitleTV.visibility = View.GONE
        historyClearBtn.visibility = View.GONE
    }

    /**
     * Показать визуальные элементы истории
     */
    private fun showHistoryViewItems(historyTracks: List<ITunesTrack>) {
        if (historyTracks.isNotEmpty()) {
            historyTitleTV.visibility = View.VISIBLE
            historyClearBtn.visibility = View.VISIBLE
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

    /**
     * Установка слушателя на кнопку "очистить строку поиска" (крестик)
     */
    private fun setClearBtnListener(prefs: SharedPreferences ) {
        clearButton.setOnClickListener {
            searchString.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchString?.windowToken, 0)
            trackDataList.clear()

            historyTracks = history.read()
            showHistoryViewItems(historyTracks)
            val historyAdapter = TrackListAdapter(historyTracks, prefs)
            trackListRV.adapter = historyAdapter
        }
    }

    /**
     * Установка наблюдателя за изменениями в строке поиска
     */
    private fun setSearchWatcher() {
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

    /**
     * Получить видимость кнопки "очистить запрос"
     */
    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    /**
     * Установка слушателя на кнопку "назад"
     */
    private fun setBackBtnListener() {
        val backBtn = findViewById<ImageView>(R.id.back)

        backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    /**
     * Инициализация списка песен
     */
    private fun initTrackList() {
        trackListRV.layoutManager = LinearLayoutManager(this)
        trackListRV.adapter = adapter
    }

    /**
     * Установка видимости элементов на странице в зависимости от статуса ответа
     */
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

    /**
     * Установка слушателя изменений в строке поиска
     */
    private fun setSearchActionListener() {
        searchString.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search(searchString)
                true
            }
            false
        }
    }

    /**
     * Установка слушателя на кнопку "обновить"
     */
    private fun setUpdateBtnListener() {
        updateBtn.setOnClickListener {
            search(searchString)
        }
    }
}