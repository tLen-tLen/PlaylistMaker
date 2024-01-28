package com.example.playlistmaker.adapters

import android.content.SharedPreferences
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.SearchHistory
import com.example.playlistmaker.dataclasses.ITunesTrack
import com.example.playlistmaker.holders.TrackViewHolder

class TrackListAdapter(
    private val trackList: List<ITunesTrack>,
    private val pref: SharedPreferences
) : RecyclerView.Adapter<TrackViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TrackViewHolder(parent)

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val item = trackList[position]

        holder.itemView.setOnClickListener { writeToHistory(item, holder) }

        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    private fun writeToHistory(track: ITunesTrack, holder: TrackViewHolder) {
        Toast.makeText(holder.itemView.context, "Добавляю в историю", Toast.LENGTH_SHORT).show()

        val history = SearchHistory(pref)
        history.write(track)
    }

}