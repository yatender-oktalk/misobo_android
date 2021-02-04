package com.misohe.misohe.mind.models

import com.google.gson.annotations.SerializedName

data class OrderPayload(
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("notes")
    val notes:Note
) {
    data class Note(
        @SerializedName("package")
        val packages:String = "Checking 1 2 3 4"
    ) {

    }
}