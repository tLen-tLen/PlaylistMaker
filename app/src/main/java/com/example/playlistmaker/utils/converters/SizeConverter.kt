package com.example.playlistmaker.utils.converters

import android.content.Context
import android.util.TypedValue

object SizeConverter {

    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }

}