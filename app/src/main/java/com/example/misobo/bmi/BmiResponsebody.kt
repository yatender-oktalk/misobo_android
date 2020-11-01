package com.example.misobo.bmi

import com.google.gson.annotations.SerializedName

data class BmiResponsebody(
    @SerializedName("data")
    val data: Data?
) {
    data class Data(
        @SerializedName("bmi")
        val bmi: Double?,
        @SerializedName("result")
        val result: String?
    )
}