package com.example.misobo

import android.content.Context

class Util {

    companion object {

        fun convertDpToPixels(dp: Float, context: Context): Int {
            val res = context.resources
            val metrics = res.displayMetrics
            val px = dp * (metrics.densityDpi / 160f)
            return px.toInt()
        }

        const val token =
            "SFMyNTY.g2gDdAAAAANkAAJpZGEBZAAKaXNfZW5hYmxlZGQABHRydWVkAAVwaG9uZW0AAAAKNzk3OTAwODIxMW4GACVYqDx1AWIAAVGA.VE2wrJJ4CzTITTdvuXMBWaJhtUVh6PM-TAaYcrNJQZY"
    }
}