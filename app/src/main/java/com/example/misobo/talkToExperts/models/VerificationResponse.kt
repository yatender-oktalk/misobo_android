package com.example.misobo.talkToExperts.models

data class VerificationResponse(
    val data: Data
) {
    data class Data(
        val id: Int?,
        val dob: String?,
        val phone: String?,
        val isEnables: Boolean?,
        val karmaPoints: Int?,
        val name: String?,
        val horoscopeId: Int?,
        val registrationId: Int?,
        val dailyReminder: String?,
        val img: String?
    )
}