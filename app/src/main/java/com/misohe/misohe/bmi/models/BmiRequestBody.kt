package com.misohe.misohe.bmi.models

import com.google.gson.annotations.SerializedName

data class BmiRequestBody(
    @SerializedName("height")
    val height: Double,
    @SerializedName("weight")
    val weight: Int,
    @SerializedName("gender")
    val gender: String
) {
}