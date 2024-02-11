package com.example.playlistmaker

import android.os.Build
import android.os.Bundle
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
        }
        setBackBtnListener()
    }

    /**
     * Отобразить данные трека
     */
    private fun setTrackData(track: ITunesTrack) {
        binding.trackTitleTv.text = track.trackName
        binding.trackArtistTv.text = track.artistName
        binding.durationTrackTv.text = DateTimeConverter.millisToMmSs(track.trackTimeMillis)
        binding.countryTrackTv.text = track.country
        binding.albumTrackTv.text = track.collectionName
        binding.genreTrackTv.text = track.primaryGenreName
        binding.yearTrackTv.text = ZonedDateTime.parse(track.releaseDate).year.toString()
        Glide.with(binding.trackImageIv)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(SizeConverter.dpToPx(2f, binding.trackImageIv.context)))
            .into(binding.trackImageIv)
    }

    /**
     * Установка слушателя на кнопку "назад"
     */
    private fun setBackBtnListener() {
        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    companion object {
        const val BUNDLE_KEY_TRACK: String = "track"
    }
}