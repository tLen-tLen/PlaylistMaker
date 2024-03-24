package com.example.playlistmaker.ui.track


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityTrackBinding
import com.example.playlistmaker.domain.enums.PlayerStatus
import com.example.playlistmaker.domain.models.ITunesTrack
import com.example.playlistmaker.utils.DateTimeConverter
import com.example.playlistmaker.utils.SizeConverter

import java.time.ZonedDateTime


class TrackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrackBinding

    private val handler = Handler(Looper.getMainLooper())

    private val playerInteractor = Creator.provideGetPlayerInteractor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getTrackUseCase = Creator.provideGetTrackUseCase(intent)
        val track = getTrackUseCase.execute()

        track?.let {
            setTrackData(track)
            preparePlayer(track)
        }

        setBackBtnListener()
        setPlayBtnListener()
    }

    override fun onPause() {
        super.onPause()
        playerInteractor.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerInteractor.releasePlayer()
        handler.removeCallbacks(playTimeRunnable)
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
            playerInteractor.playbackControl()
        }
    }

    fun preparePlayer(track: ITunesTrack) {
        if (track.previewUrl !== null) {
            playerInteractor.preparePlayer(track)
            playerInteractor.setOnCompletionListener {
                handler.removeCallbacks(playTimeRunnable)
                binding.currentTimeTv.text = DEFAULT_PLAY_TIME
            }
            playerInteractor.setAfterStartListener {
                binding.playBtn.setBackgroundResource(R.drawable.pause_btn)
                handler.post(playTimeRunnable)
            }
            playerInteractor.setAfterPauseListener {
                binding.playBtn.setBackgroundResource(R.drawable.play_btn)
                handler.removeCallbacks(playTimeRunnable)
            }
        } else {
            binding.playBtn.isEnabled = false
        }
    }

    val playTimeRunnable = object : Runnable {
        override fun run() {
            if (playerInteractor.getPlayerStatus() == PlayerStatus.STATE_PLAYING) {
                binding.currentTimeTv.text =
                    DateTimeConverter.millisToMmSs(playerInteractor.getCurrentPosition())
                handler.postDelayed(this, PLAY_TIME_DELAY)
            }
        }
    }

    companion object {
        const val BUNDLE_KEY_TRACK: String = "track"

        private const val PLAY_TIME_DELAY = 300L
        private const val DEFAULT_PLAY_TIME = "00:00"
    }
}