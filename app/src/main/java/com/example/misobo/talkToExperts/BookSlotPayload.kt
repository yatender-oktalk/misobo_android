package com.example.misobo.talkToExperts

import com.google.gson.annotations.SerializedName

data class BookSlotPayload(
    @SerializedName("start_time_unix")
    val startTime:Long?
) {
}