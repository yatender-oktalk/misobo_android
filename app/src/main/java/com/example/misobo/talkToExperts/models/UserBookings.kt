package com.example.misobo.talkToExperts.models

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
        val endTime:String?,
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