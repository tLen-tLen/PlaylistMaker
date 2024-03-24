package com.example.playlistmaker.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.TrackListAdapter
import com.example.playlistmaker.data.network.ITunesApi
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.models.ITunesTrack
import com.example.playlistmaker.data.dto.ITunesTrackResponse
import com.example.playlistmaker.domain.enums.TrackListStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    companion object {
        private const val SEARCH_STRING_KEY = "search_string"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private lateinit var binding: ActivitySearchBinding

    private var searchSavedData: String = ""

    private val trackDataList: MutableList<ITunesTrack> = mutableListOf()
    private lateinit var adapter: TrackListAdapter

    private lateinit var history: SearchHistory
    private var historyTracks = mutableListOf<ITunesTrack>()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ITunesApi::class.java)

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences(SearchHistory.HISTORY_SP, MODE_PRIVATE)
        history = SearchHistory(prefs)
        adapter = TrackListAdapter(trackDataList, prefs)

        setBackBtnListener()
        setClearBtnListener(prefs)
        setSearchWatcher()
        setSearchActionListener()
        setUpdateBtnListener()

        initTrackList()
        initHistoryList()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(searchRunnable)
    }

    /**
     * Установка слушателей на фокус строки поиска и на клик по кнопке "Очистить историю"
     */
    private fun initHistoryList() {
        historyTracks = history.read()

        binding.searchString.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.searchString.text.isEmpty()) {
                trackDataList.addAll(historyTracks)
                adapter.notifyDataSetChanged()
                showHistoryViewItems(historyTracks)
            }
        }

        binding.clearHistoryBtn.setOnClickListener {
            history.clear()
            trackDataList.clear()
            initTrackList()
            hideHistoryViewItems()
        }
    }

    /**
     * Выполнение и вывод поискового запроса
     */
    private fun search(searchString: EditText) {

        hideHistoryViewItems()
        binding.rvTracks.visibility = View.GONE
        hideErrorViewItems()
        binding.progressBar.visibility = View.VISIBLE

        val search = searchString.text.toString()
        if (search.isNotEmpty()) {
            itunesService.search(search).enqueue(object : Callback<ITunesTrackResponse> {

                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ITunesTrackResponse>,
                    response: Response<ITunesTrackResponse>
                ) {
                    binding.progressBar.visibility = View.GONE
                    if (response.code() == 200) {
                        trackDataList.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            trackDataList.addAll(response.body()!!.results)
                            initTrackList()
                            adapter.notifyDataSetChanged()
                            setTrackListStatus(TrackListStatus.SUCCESS)
                        } else {
                            setTrackListStatus(TrackListStatus.NOT_FOUND)
                        }
                    } else {
                        Log.d("ITunes search", "fail ${response.code()} ")
                        setTrackListStatus(TrackListStatus.FAIL)
                    }
                }

                override fun onFailure(call: Call<ITunesTrackResponse>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    Log.d("ITunes search", "fail connection")
                    setTrackListStatus(TrackListStatus.FAIL)
                }
            })
        }
    }

    /**
     * Скрыть визуальные элементы истории
     */
    private fun hideHistoryViewItems() {
        binding.tvHistoryTitle.visibility = View.GONE
        binding.clearHistoryBtn.visibility = View.GONE
    }

    /**
     * Показать визуальные элементы истории
     */
    private fun showHistoryViewItems(historyTracks: List<ITunesTrack>) {
        if (historyTracks.isNotEmpty()) {
            binding.rvTracks.visibility = View.VISIBLE
            binding.tvHistoryTitle.visibility = View.VISIBLE
            binding.clearHistoryBtn.visibility = View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val searchSavedData = findViewById<EditText>(R.id.search_string).text.toString()
        outState.putString(SEARCH_STRING_KEY, searchSavedData)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchSavedData = savedInstanceState.getString(SEARCH_STRING_KEY) ?: ""
    }

    /**
     * Установка слушателя на кнопку "очистить строку поиска" (крестик)
     */
    private fun setClearBtnListener(prefs: SharedPreferences) {
        binding.clearSearchBtn.setOnClickListener {
            binding.searchString.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.searchString?.windowToken, 0)
            trackDataList.clear()

            hideErrorViewItems()

            historyTracks = history.read()
            showHistoryViewItems(historyTracks)
            val historyAdapter = TrackListAdapter(historyTracks, prefs)
            binding.rvTracks.adapter = historyAdapter
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
                binding.clearSearchBtn.visibility = clearButtonVisibility(s)
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        binding.searchString.addTextChangedListener(watcher)
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
        binding.back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    /**
     * Инициализация списка песен
     */
    private fun initTrackList() {
        binding.rvTracks.layoutManager = LinearLayoutManager(this)
        binding.rvTracks.adapter = adapter
    }

    /**
     * Установка видимости элементов на странице в зависимости от статуса ответа
     */
    private fun setTrackListStatus(status: TrackListStatus) {
        when (status) {
            TrackListStatus.SUCCESS -> {
                binding.rvTracks.visibility = View.VISIBLE
                binding.errorImage.visibility = View.GONE
                binding.errorTitle.visibility = View.GONE
                binding.updateBtn.visibility = View.GONE
            }

            TrackListStatus.NOT_FOUND -> {
                binding.rvTracks.visibility = View.GONE
                binding.errorImage.setImageResource(R.drawable.not_found)
                binding.errorTitle.setText(R.string.not_found)
                binding.errorImage.visibility = View.VISIBLE
                binding.errorTitle.visibility = View.VISIBLE
            }

            else -> {
                binding.rvTracks.visibility = View.GONE
                binding.errorImage.setImageResource(R.drawable.network_error)
                binding.errorTitle.setText(R.string.network_error)

                binding.errorImage.visibility = View.VISIBLE
                binding.errorTitle.visibility = View.VISIBLE
                binding.updateBtn.visibility = View.VISIBLE
            }
        }
    }

    /**
     * Установка слушателя изменений в строке поиска
     */
    private fun setSearchActionListener() {
        binding.searchString.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search(binding.searchString)
                true
            }
            false
        }
    }

    /**
     * Установка слушателя на кнопку "обновить"
     */
    private fun setUpdateBtnListener() {
        binding.updateBtn.setOnClickListener {
            search(binding.searchString)
        }
    }


    /**
     * Скрыть картинку и заголовок ошибки
     */
    private fun hideErrorViewItems() {
        binding.errorImage.visibility = View.GONE
        binding.errorTitle.visibility = View.GONE
        binding.updateBtn.visibility = View.GONE
    }

    /**
     * Метод, выполняющийся при вводе текста
     */
    private val searchRunnable = Runnable {
        if (binding.searchString.text.isNotEmpty()) {
            search(binding.searchString)
        }
    }

    /**
     * Добавить в очередь вызов метода, выполняющегося при вводе текста
     */
    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }
}