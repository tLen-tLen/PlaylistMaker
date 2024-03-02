package com.example.playlistmaker.adapters

import android.content.Intent
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.SearchHistory
import com.example.playlistmaker.TrackActivity
import com.example.playlistmaker.dataclasses.ITunesTrack
import com.example.playlistmaker.holders.TrackViewHolder
import com.google.gson.Gson

class TrackListAdapter(
    private val trackList: List<ITunesTrack>,
    private val pref: SharedPreferences
) : RecyclerView.Adapter<TrackViewHolder> () {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TrackViewHolder(parent)

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val item = trackList[position]

        holder.itemView.setOnClickListener {
            if (clickDebounce()) {
                writeToHistory(item)

                val intent = Intent(holder.itemView.context, TrackActivity::class.java)
                intent.putExtra(TrackActivity.BUNDLE_KEY_TRACK, item)
                holder.itemView.context.startActivity(intent)
            }
        }

        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    private fun writeToHistory(track: ITunesTrack) {
        val history = SearchHistory(pref)
        history.write(track)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed

        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true}, CLICK_DEBOUNCE_DELAY)
        }

        return current
    }

}