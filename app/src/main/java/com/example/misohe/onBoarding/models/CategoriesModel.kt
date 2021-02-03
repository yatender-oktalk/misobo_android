package com.example.misohe.onBoarding.models

import com.google.gson.annotations.SerializedName

data class CategoriesModel(
    @SerializedName("data")
    val data: List<Category>?
) {
    data class Category(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("desc")
        val description: String?,
        @SerializedName("sub_category")
        val subCategory: List<SubCategory>?,
        var isSelected:Boolean = false
    )

    data class SubCategory(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("name")
        val name: String?
    )
}