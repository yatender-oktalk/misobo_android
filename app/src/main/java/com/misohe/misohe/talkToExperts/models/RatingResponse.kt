package com.misohe.misohe.talkToExperts.models

import com.google.gson.annotations.SerializedName

data class RatingResponse(
    @SerializedName("data")
    val data:String?
) {
}