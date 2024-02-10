package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityTrackBinding
import com.example.playlistmaker.dataclasses.ITunesTrack
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.util.Locale


class TrackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val trackJson = intent.getStringExtra("track")
        val track = Gson().fromJson(trackJson, ITunesTrack::class.java)

        setTrackData(track)
        setBackBtnListener()
    }

    /**
     * Отобразить данные трека
     */
    private fun setTrackData(track: ITunesTrack) {
        binding.trackTitleTv.text = track.trackName
        binding.trackArtistTv.text = track.artistName
        binding.durationTrackTv.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        binding.countryTrackTv.text = track.country
        binding.albumTrackTv.text = track.collectionName
        binding.genreTrackTv.text = track.primaryGenreName
        binding.yearTrackTv.text = ZonedDateTime.parse(track.releaseDate).year.toString()
        Glide.with(binding.trackImageIv)
            .load(track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"))
            .placeholder(R.drawable.baseline_replay_circle_filled_24)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2f, binding.trackImageIv.context)))
            .into(binding.trackImageIv)
    }

    //todo вынести
    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }

    /**
     * Установка слушателя на кнопку "назад"
     */
    private fun setBackBtnListener() {
        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}