package com.example.playlistmaker.data.repository

import android.content.Intent
import android.os.Build
import com.example.playlistmaker.domain.models.ITunesTrack
import com.example.playlistmaker.ui.track.TrackActivity
import com.example.vehicle_shop_clean.domain.repository.TrackInfoRepository

class TrackInfoRepositoryImpl(private val intent: Intent): TrackInfoRepository {

    override fun getTrack(): ITunesTrack {
        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(TrackActivity.BUNDLE_KEY_TRACK, ITunesTrack::class.java)
        } else {
            intent.getParcelableExtra(TrackActivity.BUNDLE_KEY_TRACK)
        }

        return track!!
    }
}