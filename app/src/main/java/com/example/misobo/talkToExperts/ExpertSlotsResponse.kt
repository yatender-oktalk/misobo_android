package com.example.misobo.talkToExperts

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