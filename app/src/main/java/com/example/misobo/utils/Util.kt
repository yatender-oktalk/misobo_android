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

        fun toTitleCase(givenString: String): String? {
            val arr = givenString.trim().replace("\\s+" , " ").split(" ".toRegex()).toTypedArray()
            val sb = StringBuffer()
            if(arr.isNotEmpty()) {
                for (i in arr.indices) {
                    sb.append(Character.toUpperCase(arr[i][0]))
                        .append(arr[i].substring(1)).append(" ")
                }
            }
            return sb.toString().trim { it <= ' ' }
        }
    }


}