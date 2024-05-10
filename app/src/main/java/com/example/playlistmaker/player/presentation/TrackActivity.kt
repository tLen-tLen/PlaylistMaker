package com.example.playlistmaker.player.presentation


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityTrackBinding
import com.example.playlistmaker.search.domain.models.ITunesTrack
import com.example.playlistmaker.utils.converters.DateTimeConverter
import com.example.playlistmaker.utils.converters.SizeConverter

import java.time.ZonedDateTime


class TrackActivity : AppCompatActivity() {

    private lateinit var viewModel: PlayerViewModel

    private lateinit var binding: ActivityTrackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel =
            ViewModelProvider(this, PlayerViewModel.getViewModelFactory(intent))[PlayerViewModel::class.java]
        viewModel.observeState().observe(this) {
            render(it)
        }

        viewModel.preparePlayer()
        setPlayBtnListener()

        setTrackData(viewModel.getTrack())

        setBackBtnListener()
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.releasePlayer()
    }

    private fun render(state: PlayerScreenState) {
        when (state) {
            is PlayerScreenState.DefaultScreenState -> showDefaultScreen()
            is PlayerScreenState.PreparedScreenState -> showPreparedScreen()
            is PlayerScreenState.PlayingScreenState -> showPlayingScreen()
            is PlayerScreenState.PauseScreenState -> showPauseScreen()
            is PlayerScreenState.TimerState -> setPlaybackTime(state.playbackTimer)
        }
    }

    private fun showDefaultScreen() {
        binding.playBtn.setBackgroundResource(R.drawable.play_btn)
        binding.playBtn.isEnabled = false
    }

    private fun showPreparedScreen() {
        binding.playBtn.setBackgroundResource(R.drawable.play_btn)
        binding.playBtn.isEnabled = true
        binding.currentTimeTv.text = DEFAULT_PLAY_TIME
    }

    private fun showPlayingScreen() {
        binding.playBtn.setBackgroundResource(R.drawable.pause_btn)
        binding.playBtn.isEnabled = true
    }

    private fun showPauseScreen() {
        binding.playBtn.setBackgroundResource(R.drawable.play_btn)
        binding.playBtn.isEnabled = true
    }

    private fun setPlaybackTime(time: String) {
        binding.currentTimeTv.text = time
    }

    /**
     * Отобразить данные трека
     */
    private fun setTrackData(track: ITunesTrack) {
        with(binding) {
            trackTitleTv.text = track.trackName
            trackArtistTv.text = track.artistName
            durationTrackTv.text = DateTimeConverter.millisToMmSs(track.trackTimeMillis)
            countryTrackTv.text = track.country
            albumTrackTv.text = track.collectionName
            genreTrackTv.text = track.primaryGenreName
            yearTrackTv.text = ZonedDateTime.parse(track.releaseDate).year.toString()
            Glide.with(trackImageIv)
                .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .transform(RoundedCorners(SizeConverter.dpToPx(2f, trackImageIv.context)))
                .into(trackImageIv)
        }
    }

    /**
     * Установка слушателя на кнопку "назад"
     */
    private fun setBackBtnListener() {
        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    /**
     * Слушатель кнопки "играть"/"пауза"
     */
    private fun setPlayBtnListener() {
        binding.playBtn.setOnClickListener {
            viewModel.playbackControl()
        }
    }

    companion object {
        private const val DEFAULT_PLAY_TIME = "00:00"
    }
}