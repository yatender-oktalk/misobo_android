package com.misohe.misohe.talkToExperts.models

import com.google.gson.annotations.SerializedName

data class ExpertSlotsResponse(
    @SerializedName("date")
    val date: String?,
    @SerializedName("is_booked")
    val isBooked: Boolean?,
    @SerializedName("unix_time")
    val unixTime: Long?
) {
}