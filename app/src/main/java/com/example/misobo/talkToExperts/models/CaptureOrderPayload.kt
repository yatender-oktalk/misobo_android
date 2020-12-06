package com.example.misobo.talkToExperts.models

import com.google.gson.annotations.SerializedName

data class CaptureOrderPayload(
    @SerializedName("payment_id")
    val paymentId: String,
    @SerializedName("signature")
    val signature: String,
    @SerializedName("order_id")
    val orderId: Int,
    @SerializedName("transaction_id")
    val transactionid: Int,
    @SerializedName("amount")
    val amount: Double
) {
}