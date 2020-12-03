package com.example.misobo.blogs

import com.google.gson.annotations.SerializedName

data class BlogsModel(
    @SerializedName("data")
    val data: List<Blogs>?
) {
    data class Blogs(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("category")
        val category: String?,
        @SerializedName("content")
        val content: String?,
        @SerializedName("is_enabled")
        val isEnabled: Boolean?,
        @SerializedName("title")
        val title: String?,
        @SerializedName("image")
        val image: Boolean?
    )
}