package com.example.misobo.talkToExperts

data class VerificationResponse(
    val data: Data
) {
    data class Data(
        val id: Int?,
        val dob: String?,
        val phone: String?,
        val isEnables: Boolean?,
        val karmaPoints: Long?,
        val name: String?,
        val horoscopeId: Int?,
        val registrationId: Int?,
        val dailyReminder: String?,
        val img: String?
    )
}