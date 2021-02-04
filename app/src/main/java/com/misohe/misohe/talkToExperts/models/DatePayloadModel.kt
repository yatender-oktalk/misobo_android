package com.misohe.misohe.talkToExperts.models

import com.google.gson.annotations.SerializedName

data class DatePayloadModel(
    @SerializedName("date")
    val date:String
) {
}