package com.example.misohe.onBoarding.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("data")
    val data: Data?
) {
    data class Data(
        @SerializedName("id")
        val registrationId: Int?,
        @SerializedName("token")
        val token: String?,
        @SerializedName("user_id")
        val userId:Int?
    )
}