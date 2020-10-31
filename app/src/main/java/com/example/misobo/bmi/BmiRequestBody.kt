package com.example.misobo.bmi

import com.google.gson.annotations.SerializedName

data class BmiRequestBody(
    @SerializedName("height")
    val height: Double,
    @SerializedName("weight")
    val weight: Int
) {
}