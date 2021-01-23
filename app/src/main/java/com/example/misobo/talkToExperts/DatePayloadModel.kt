package com.example.misobo.talkToExperts

import com.google.gson.annotations.SerializedName

data class DatePayloadModel(
    @SerializedName("date")
    val date:String
) {
}