package com.example.misobo.onBoarding

import com.google.gson.annotations.SerializedName

data class RegistrationModel(
    @SerializedName("device_id")
    val deviceId: String?
) {
}