package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityTrackBinding
import com.example.playlistmaker.dataclasses.ITunesTrack
import com.example.playlistmaker.utils.DateTimeConverter
import com.example.playlistmaker.utils.SizeConverter

import java.time.ZonedDateTime


class TrackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrackBinding

    private var mediaPlayer = MediaPlayer()

    private var playerState = STATE_DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(BUNDLE_KEY_TRACK, ITunesTrack::class.java)
        } else {
            intent.getParcelableExtra(BUNDLE_KEY_TRACK)
        }

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
            }
        } else {
            Toast.makeText(applicationContext, "Превью отсутствует", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Начать воспроизведение
     */
    private fun startPlayer() {
        mediaPlayer.start()
        binding.playBtn.setBackgroundResource(R.drawable.pause_btn)
        playerState = STATE_PLAYING
    }

    /**
     * Остановить воспроизведение
     */
    private fun pausePlayer() {
        mediaPlayer.pause()
        binding.playBtn.setBackgroundResource(R.drawable.play_btn)
        playerState = STATE_PAUSED
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


    companion object {
        const val BUNDLE_KEY_TRACK: String = "track"

        // состояния медиа плеера
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}