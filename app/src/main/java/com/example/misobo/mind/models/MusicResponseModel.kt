package com.example.misobo.mind.models

import com.google.gson.annotations.SerializedName

data class MusicResponseModel(
    @SerializedName("entries")
    val entries: List<MusicModel>?
) {
    data class MusicModel(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("author_name")
        val authorName: String?,
        @SerializedName("duration")
        val duration: Int?,
        @SerializedName("hls_url")
        val hlsUrl: String?,
        @SerializedName("karma")
        val karma: Int?,
        @SerializedName("progress")
        var progress: Int?,
        @SerializedName("production_name")
        val productionName: String?,
        @SerializedName("title")
        val title: String?,
        @SerializedName("image")
        val image:String?,
        @SerializedName("url")
        val url: String?,
        @SerializedName("tag")
        val tag:String?
    )
}