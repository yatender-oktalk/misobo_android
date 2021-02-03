package com.example.misohe.talkToExperts.models

import com.google.gson.annotations.SerializedName

data class CapturePaymentResponse(
    @SerializedName("data")
    val data: Data?
) {
    data class Data(
        @SerializedName("msg")
        val msg: String?
    )
}