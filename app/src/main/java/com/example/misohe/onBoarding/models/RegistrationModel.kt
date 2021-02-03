package com.example.misohe.onBoarding.models

import com.google.gson.annotations.SerializedName

data class RegistrationModel(
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("device_id")
    val deviceId: String?
) {

}