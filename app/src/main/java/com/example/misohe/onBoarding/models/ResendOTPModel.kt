package com.example.misohe.onBoarding.models

import com.google.gson.annotations.SerializedName

data class ResendOTPModel(
    @SerializedName("phone")
    val phone: String?
) {
}