package com.example.misobo.bmi.models

import com.google.gson.annotations.SerializedName

data class BmiRequestBody(
    @SerializedName("height")
    val height: Double,
    @SerializedName("weight")
    val weight: Int
) {
}