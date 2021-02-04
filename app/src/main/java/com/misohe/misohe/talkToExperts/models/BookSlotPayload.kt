package com.misohe.misohe.talkToExperts.models

import com.google.gson.annotations.SerializedName

data class BookSlotPayload(
    @SerializedName("start_time_unix")
    val startTime:Long?
) {
}