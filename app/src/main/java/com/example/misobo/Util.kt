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
            "SFMyNTY.g2gDdAAAAANkAAJpZGEDZAAKaXNfZW5hYmxlZGQABHRydWVkAAVwaG9uZW0AAAAKODk2OTcxMDQwNG4GAD3hJEJ1AWIAAVGA.KNfH95yN4-gj85bfimaD8TdNjKDUEryZYfaYmfpc8R0"
    }
}