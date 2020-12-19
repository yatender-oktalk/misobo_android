package com.example.misobo.talkToExperts.models

import com.google.gson.annotations.SerializedName

data class RatingResponse(
    @SerializedName("data")
    val data:String?
) {
}