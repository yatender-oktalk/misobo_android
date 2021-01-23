package com.example.misobo.talkToExperts.models

import com.google.gson.annotations.SerializedName

data class RatingPayload(
    @SerializedName("booking_id")
    val bookingId: Int,
    @SerializedName("rating")
    val rating: Int
) {
}