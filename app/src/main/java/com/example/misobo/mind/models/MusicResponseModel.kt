package com.example.misobo.mind.models

import com.google.gson.annotations.SerializedName

data class MusicResponseModel(
    @SerializedName("entries")
    val entries: List<MusicModel>?
) {
    data class MusicModel(
        @SerializedName("entries")
        val id: Int?,
        @SerializedName("author_name")
        val authorName: String?,
        @SerializedName("duration")
        val duration: Int?,
        @SerializedName("hls_url")
        val hlsUrl: String?,
        @SerializedName("karma")
        val karma: Int?,
        @SerializedName("production_name")
        val productionName: String?,
        @SerializedName("title")
        val title: String?,
        @SerializedName("url")
        val url: String?
    )
}