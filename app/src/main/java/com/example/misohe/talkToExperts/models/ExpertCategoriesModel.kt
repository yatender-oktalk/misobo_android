package com.example.misohe.talkToExperts.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ExpertCategoriesModel(
    @SerializedName("id")
    val id:Int?,
    @SerializedName("name")
    val name:String?
):Parcelable {
}