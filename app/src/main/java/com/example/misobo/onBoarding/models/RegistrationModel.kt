package com.example.misobo.onBoarding.models

import com.google.gson.annotations.SerializedName

data class RegistrationModel(
    @SerializedName("device_id")
    val deviceId: String?
) {
}