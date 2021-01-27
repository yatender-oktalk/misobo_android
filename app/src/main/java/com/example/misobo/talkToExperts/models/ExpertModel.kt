package com.example.misobo.talkToExperts.models

import com.google.gson.annotations.SerializedName


data class ExpertModel(
    @SerializedName("entries")
    val entries: List<Expert>?,
    @SerializedName("page_number")
    val pageNumber:Int?,
    @SerializedName("total_pages")
    val totalPages:Int?

) {
    data class Expert(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("experience")
        val experience: String?,
        @SerializedName("expertise")
        val expertise: String?,
        @SerializedName("img")
        val image: String?,
        @SerializedName("language")
        val language: String?,
        @SerializedName("rating")
        val rating: String?,
        @SerializedName("qualification")
        val qualification: String?,
        @SerializedName("total_consultations")
        val consultations: Int?,
        @SerializedName("consult_karma")
        val karmaCoinsNeeded:Int?
    )
}