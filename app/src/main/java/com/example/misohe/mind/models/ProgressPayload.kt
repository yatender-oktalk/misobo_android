package com.example.misohe.mind.models

import com.google.gson.annotations.SerializedName

data class ProgressPayload(
    @SerializedName("user_id")
    val userId: Int?,
    @SerializedName("progress")
    val progress: Long?
) {

}