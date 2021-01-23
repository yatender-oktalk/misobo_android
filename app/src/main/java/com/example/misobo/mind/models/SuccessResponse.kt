package com.example.misobo.mind.models

import com.google.gson.annotations.SerializedName

data class SuccessResponse(
    @SerializedName("data")
    val data:String?
) {
}