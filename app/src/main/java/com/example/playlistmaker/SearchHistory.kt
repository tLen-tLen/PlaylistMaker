package com.example.playlistmaker

import android.content.SharedPreferences
import android.util.Log
import com.example.playlistmaker.dataclasses.ITunesTrack
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(val sharedPreferences: SharedPreferences) {

    private var trackList: MutableList<ITunesTrack> = mutableListOf()
    fun read(): MutableList<ITunesTrack> {
        val json = sharedPreferences.getString(HISTORY_TRACK_LIST, null) ?: return mutableListOf()
        Log.d("TEST", "read: $json")
        trackList = Gson().fromJson(json, Array<ITunesTrack>::class.java).toCollection(ArrayList())

        return trackList
    }

    fun write(addedTrack: ITunesTrack? = null): MutableList<ITunesTrack> {

        // TODO проверки проверить
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

        return trackList
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        const val HISTORY_TRACK_LIST = "HISTORY_TRACK_LIST"
        const val HISTORY_SP = "HISTORY_SP"
        const val HISTORY_LIMIT = 10
    }
}