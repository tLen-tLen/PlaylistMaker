package com.example.playlistmaker.holders

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.dataclasses.ITunesTrack
import com.example.playlistmaker.utils.DateTimeConverter
import com.example.playlistmaker.utils.SizeConverter

class TrackViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.track, parent, false)
) {

    // TODO binding
    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val artistName: TextView = itemView.findViewById(R.id.artist_name)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)
    private val trackImage: ImageView = itemView.findViewById(R.id.track_image)

    fun bind(track: ITunesTrack) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = DateTimeConverter.millisToMmSs(track.trackTimeMillis)

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(SizeConverter.dpToPx(2f, itemView.context)))
            .into(trackImage)
    }
}