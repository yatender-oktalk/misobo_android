package com.example.misobo.talkToExperts.models

import com.google.gson.annotations.SerializedName

data class OtpPayload(
    @SerializedName("phone")
    val phone: String,
    @SerializedName("otp")
    val otp: Int? = null,
    @SerializedName("id")
    val id: Int? = null
) {
}