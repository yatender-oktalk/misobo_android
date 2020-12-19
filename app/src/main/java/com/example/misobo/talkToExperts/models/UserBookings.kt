package com.example.misobo.talkToExperts.models

import com.google.gson.annotations.SerializedName

data class UserBookings(
    val data:Data?
) {
    data class Data(
        val entries:List<Entry>?
    )

    data class Entry(
        val id:Int?,
        val karma:Int?,
        val expertId:Int?,
        val startTime:String?,
        @SerializedName("end_time")
        val endTime:String?,
        @SerializedName("is_rated")
        val isRated:Boolean?,
        val expert:ExpertModel.Expert?
    )

    data class Expert(
        val id: Int?,
        val name:String?,
        val about:String?,
        val consultKarma:Int?,
        val experience:String?,
        val img:String?,
        val isEnables:Boolean?,
        val language:String?,
        val rating:String?,
        val totalConsultations:String?

    )
}