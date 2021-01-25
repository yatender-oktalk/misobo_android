package com.example.misobo.utils

import android.content.Context

class Util {
    companion object {
        fun convertDpToPixels(dp: Float, context: Context): Int {
            val res = context.resources
            val metrics = res.displayMetrics
            val px = dp * (metrics.densityDpi / 160f)
            return px.toInt()
        }
    }
}