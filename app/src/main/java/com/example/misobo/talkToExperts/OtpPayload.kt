package com.example.misobo.talkToExperts

import com.google.gson.annotations.SerializedName

data class OtpPayload(
    @SerializedName("phone")
    val phone: String,
    @SerializedName("otp")
    val otp: Int?=null
) {
}