package com.example.misobo.talkToExperts.models

import com.google.gson.annotations.SerializedName


data class ExpertModel(
    @SerializedName("entries")
    val entries: List<Expert>?
) {
    data class Expert(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("experience")
        val experience: String?,
        @SerializedName("image")
        val image: String?,
        @SerializedName("language")
        val language: String?,
        @SerializedName("rating")
        val rating: String?,
        @SerializedName("total_consultations")
        val consultations: Long?
    )
}