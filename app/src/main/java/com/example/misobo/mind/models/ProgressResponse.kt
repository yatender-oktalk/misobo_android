package com.example.misobo.mind.models

import com.google.gson.annotations.SerializedName

data class ProgressResponse(
    @SerializedName("data")
    val data:Details
) {
    data class Details(
        @SerializedName("id")
        val id:Int,
        @SerializedName("progress")
        val progress:Long?,
        @SerializedName("user_id")
        val userId:Int?,
        @SerializedName("music_id")
        val musicId:Int?,
        @SerializedName("inserted_at")
        val insertedAt:String?,
        @SerializedName("updated_at")
        val updatedAt:String?
    )
}