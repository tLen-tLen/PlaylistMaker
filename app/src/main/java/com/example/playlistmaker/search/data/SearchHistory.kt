package com.example.playlistmaker.search.data

import android.content.SharedPreferences
import com.example.playlistmaker.search.domain.models.ITunesTrack
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(private val sharedPreferences: SharedPreferences) {

    fun read(): ArrayList<ITunesTrack> {
        val json = sharedPreferences.getString(HISTORY_TRACK_LIST, null) ?: return arrayListOf()

        return Gson().fromJson(json, Array<ITunesTrack>::class.java).toCollection(ArrayList())
    }

    fun write(addedTrack: ITunesTrack? = null) {
        if (addedTrack != null) {
            val editor = sharedPreferences.edit()

            val json = sharedPreferences.getString(HISTORY_TRACK_LIST, null)

            val currentHistory: MutableList<ITunesTrack> =
                Gson().fromJson(json, object : TypeToken<MutableList<ITunesTrack>>() {}.type)
                    ?: mutableListOf()


            currentHistory.removeAll { it.trackId == addedTrack.trackId }

            currentHistory.add(0, addedTrack)

            while (currentHistory.size > HISTORY_LIMIT) {
                currentHistory.removeAt(currentHistory.lastIndex)
            }

            val jsonHistory = Gson().toJson(currentHistory)
            editor.putString(HISTORY_TRACK_LIST, jsonHistory)
            editor.apply()
        }
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        private const val HISTORY_TRACK_LIST = "HISTORY_TRACK_LIST"
        private const val HISTORY_LIMIT = 10
        const val HISTORY_SP = "HISTORY_SP"
    }
}