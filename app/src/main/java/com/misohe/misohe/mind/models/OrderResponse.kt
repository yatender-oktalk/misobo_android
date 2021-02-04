package com.misohe.misohe.mind.models

import com.google.gson.annotations.SerializedName

data class OrderResponse(
    @SerializedName("data")
    val data: Data?
) {
    data class Data(
        @SerializedName("order_id")
        val orderId: Int?,
        @SerializedName("pg_order_id")
        val paymentGatewayOrderId: String?,
        @SerializedName("transaction_id")
        val transactionId: Int?
    ) {}
}