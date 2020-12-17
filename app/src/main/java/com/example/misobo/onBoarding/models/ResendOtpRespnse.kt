package com.example.misobo.onBoarding.models

import com.google.gson.annotations.SerializedName

data class ResendOtpRespnse(
    @SerializedName("data")
    val data: String?
) {
}