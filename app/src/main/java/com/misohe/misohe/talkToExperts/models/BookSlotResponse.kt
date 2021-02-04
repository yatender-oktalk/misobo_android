package com.misohe.misohe.talkToExperts.models

import com.google.gson.annotations.SerializedName

data class BookSlotResponse(
    @SerializedName("data")
    val data: Data?
) {
    data class Data(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("karma")
        val karma: Int?,
        @SerializedName("expert_id")
        val expertId: Int?,
        @SerializedName("user_id")
        val userId: Int?,
        @SerializedName("start_time")
        val startTime: String?,
        @SerializedName("end_time")
        val endTime: String?
    )
}