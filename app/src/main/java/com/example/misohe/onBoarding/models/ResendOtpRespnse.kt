package com.example.misohe.onBoarding.models

import com.google.gson.annotations.SerializedName

data class ResendOtpRespnse(
    @SerializedName("data")
    val data: Data?
) {
    data class Data(
        @SerializedName("id")
        val id:Int?,
        @SerializedName("msg")
        val msg:String?,
        @SerializedName("user_id")
        val userId:Int?
    )
}