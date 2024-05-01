package com.example.playlistmaker.player.data.repository

import android.content.Intent
import android.os.Build
import com.example.playlistmaker.search.domain.models.ITunesTrack
import com.example.playlistmaker.utils.Consts
import com.example.playlistmaker.player.domain.repository.TrackInfoRepository

class TrackInfoRepositoryImpl(private val intent: Intent): TrackInfoRepository {

    override fun getTrack(): ITunesTrack {
        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(Consts.getBundleKeyForTrack(), ITunesTrack::class.java)
        } else {
            intent.getSerializableExtra(Consts.getBundleKeyForTrack())
        }

        return (track as ITunesTrack?)!!
    }
}