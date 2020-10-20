package com.example.misobo.onBoarding

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("data")
    val data: Data?
) {
    data class Data(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("token")
        val token: String?
    )
}