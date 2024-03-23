package com.example.playlistmaker.ui.track

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityTrackBinding
import com.example.playlistmaker.domain.models.ITunesTrack
import com.example.playlistmaker.utils.DateTimeConverter
import com.example.playlistmaker.utils.SizeConverter

import java.time.ZonedDateTime


class TrackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrackBinding

    private val mediaPlayer = MediaPlayer()

    private var playerState = STATE_DEFAULT

    private val handler = Handler(Looper.getMainLooper())

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
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
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
     * Подготовка медиа плеера
     */
    private fun preparePlayer(track: ITunesTrack) {
        if (track.previewUrl !== null) {
            mediaPlayer.setDataSource(track.previewUrl)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                playerState = STATE_PREPARED
            }
            mediaPlayer.setOnCompletionListener {
                playerState = STATE_PREPARED
                handler.removeCallbacks(playTimeRunnable)
                binding.currentTimeTv.text = DEFAULT_PLAY_TIME
            }
        } else {
            binding.playBtn.isEnabled = false
        }
    }

    /**
     * Начать воспроизведение
     */
    private fun startPlayer() {
        mediaPlayer.start()
        binding.playBtn.setBackgroundResource(R.drawable.pause_btn)
        playerState = STATE_PLAYING
        handler.post(playTimeRunnable)
    }

    /**
     * Остановить воспроизведение
     */
    private fun pausePlayer() {
        mediaPlayer.pause()
        binding.playBtn.setBackgroundResource(R.drawable.play_btn)
        playerState = STATE_PAUSED
        handler.removeCallbacks(playTimeRunnable)
    }

    /**
     * Контроль воспроизведения
     */
    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    /**
     * Слушатель кнопки "играть"/"пауза"
     */
    private fun setPlayBtnListener() {
        binding.playBtn.setOnClickListener {
            playbackControl()
        }
    }

    private val playTimeRunnable = object : Runnable {
        override fun run() {
            if (playerState == STATE_PLAYING) {
                binding.currentTimeTv.text =
                    DateTimeConverter.millisToMmSs(mediaPlayer.currentPosition)
                handler.postDelayed(this, PLAY_TIME_DELAY)
            }
        }
    }


    companion object {
        const val BUNDLE_KEY_TRACK: String = "track"

        // состояния медиа плеера
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        private const val PLAY_TIME_DELAY = 300L
        private const val DEFAULT_PLAY_TIME = "00:00"
    }
}