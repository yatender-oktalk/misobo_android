package com.example.misobo.myProfile

import com.google.gson.annotations.SerializedName

data class NamePayload(
    @SerializedName("data")
    val data: Data?
) {
    data class Data(
        @SerializedName("name")
        val name: String?
    )

}