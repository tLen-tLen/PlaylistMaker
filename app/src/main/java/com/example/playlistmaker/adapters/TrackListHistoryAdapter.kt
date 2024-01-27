package com.example.playlistmaker.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.playlistmaker.dataclasses.ITunesTrack
import com.example.playlistmaker.holders.TrackViewHolder

class TrackListHistoryAdapter(
    private val historyList: List<ITunesTrack>
) : RecyclerView.Adapter<TrackViewHolder> () {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TrackViewHolder(parent)

    override fun getItemCount(): Int = historyList.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val item = historyList[position]
        holder.bind(item)
    }

}