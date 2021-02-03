package com.example.misohe.onBoarding.models

import com.google.gson.annotations.SerializedName

data class CategoriesRequestModel(
    @SerializedName("categories")
    val categories: List<Int?>? = null,
    @SerializedName("sub_categories")
    val subCategories: List<Int?>? = null
) {
}