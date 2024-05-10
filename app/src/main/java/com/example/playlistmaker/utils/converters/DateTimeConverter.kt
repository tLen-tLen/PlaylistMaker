package com.example.playlistmaker.utils.converters

import java.text.SimpleDateFormat
import java.util.Locale

object DateTimeConverter {
    fun millisToMmSs(millis: Int): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(millis)
}