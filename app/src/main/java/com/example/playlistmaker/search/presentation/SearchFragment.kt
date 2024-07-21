package com.example.playlistmaker.search.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.search.domain.models.ITunesTrack
import com.example.playlistmaker.player.domain.enums.TrackListStatus
import com.example.playlistmaker.search.data.SearchHistoryImpl
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModel()

    private val trackDataList: MutableList<ITunesTrack> = mutableListOf()
    private lateinit var adapter: TrackListAdapter

    private lateinit var history: SearchHistoryImpl
    private var historyTracks = mutableListOf<ITunesTrack>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        val prefs = requireActivity().getSharedPreferences(SearchHistoryImpl.HISTORY_SP, MODE_PRIVATE)
        history = SearchHistoryImpl(prefs)
        adapter = TrackListAdapter(trackDataList, prefs)

        setClearBtnListener(prefs)
        setSearchWatcher()
        setSearchActionListener()
        setUpdateBtnListener()

        initTrackList()
        initHistoryList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.removeCallbacks()
    }

    private fun render(state: SearchScreenState) {
        when (state) {
            is SearchScreenState.Loading -> showProgressBar()
            is SearchScreenState.SearchContent -> showContent(state.tracks)
            is SearchScreenState.HistoryContent -> showHistoryContent(state.tracks)
            is SearchScreenState.EmptySearch -> showEmptySearch()
            is SearchScreenState.Error -> showSearchError()
            is SearchScreenState.EmptyScreen -> showEmptyScreen()
        }
    }

    private fun showEmptyScreen() {
        binding.searchContainer.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        binding.rvTracks.visibility = View.GONE
        hideHistoryViewItems()
        hideErrorViewItems()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showContent(tracks: List<ITunesTrack>) {
        binding.clearHistoryBtn.visibility = View.GONE
        setTrackListStatus(TrackListStatus.SUCCESS)
        adapter.trackList.clear()
        adapter.trackList.addAll(tracks)
        initTrackList()
        adapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showHistoryContent(tracks: List<ITunesTrack>) {
        binding.clearHistoryBtn.visibility = View.VISIBLE
        setTrackListStatus(TrackListStatus.SUCCESS)
        adapter.trackList.clear()
        adapter.trackList.addAll(tracks)
        initHistoryList()
        adapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showEmptySearch() {
        binding.clearHistoryBtn.visibility = View.GONE
        setTrackListStatus(TrackListStatus.NOT_FOUND)
        adapter.trackList.clear()
        initTrackList()
        adapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showSearchError() {
        binding.clearHistoryBtn.visibility = View.GONE
        setTrackListStatus(TrackListStatus.FAIL)
        adapter.trackList.clear()
        initTrackList()
        adapter.notifyDataSetChanged()
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

    /**
     * Установка слушателя на кнопку "очистить строку поиска" (крестик)
     */
    private fun setClearBtnListener(prefs: SharedPreferences) {
        binding.clearSearchBtn.setOnClickListener {
            binding.searchString.setText("")
            val inputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
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
                viewModel.searchDebounce(s.toString())
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
     * Инициализация списка песен
     */
    private fun initTrackList() {
        binding.rvTracks.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvTracks.adapter = adapter
    }

    /**
     * Установка видимости элементов на странице в зависимости от статуса ответа
     */
    private fun setTrackListStatus(status: TrackListStatus) {
        binding.progressBar.visibility = View.GONE
        when (status) {
            TrackListStatus.SUCCESS -> {
                binding.searchContainer.visibility = View.VISIBLE
                binding.rvTracks.visibility = View.VISIBLE
                binding.errorImage.visibility = View.GONE
                binding.errorTitle.visibility = View.GONE
                binding.updateBtn.visibility = View.GONE
            }

            TrackListStatus.NOT_FOUND -> {
                binding.searchContainer.visibility = View.VISIBLE
                binding.rvTracks.visibility = View.GONE
                binding.errorImage.setImageResource(R.drawable.not_found)
                binding.errorTitle.setText(R.string.not_found)
                binding.errorImage.visibility = View.VISIBLE
                binding.errorTitle.visibility = View.VISIBLE
            }

            else -> {
                binding.searchContainer.visibility = View.VISIBLE
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
                viewModel.search(binding.searchString.toString())
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
            viewModel.search(binding.searchString.toString())
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
}