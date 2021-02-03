package com.example.misohe.myProfile

import com.google.gson.annotations.SerializedName

data class ProfileResponseModel(
    @SerializedName("data")
    val data: Data?,
    @SerializedName("msg")
    val message: String?,
    @SerializedName("is_new_user")
    val isNewUser: Boolean? = false
) {
    data class Data(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("dob")
        val dob: Int?,
        @SerializedName("phone")
        val phone: String?,
        @SerializedName("karma_points")
        val karmaPoints: String,
        @SerializedName("name")
        val name: String?,
        @SerializedName("registration_id")
        val registrationId: Int?,
        @SerializedName("img")
        val image: String?,
        @SerializedName("weight")
        val weight: Int?,
        @SerializedName("bmi")
        val bmi: Double?,
        @SerializedName("height")
        val height: String?,
        @SerializedName("login_streak_days")
        val loginStreakDays: Int?,
        @SerializedName("login_streak")
        val loginStreak: Streak?,
        @SerializedName("bmi_checked_at")
        val bmiCheckedDate: String?,
        @SerializedName("is_body_pack_unlocked")
        val isBodyPackUnlocked: Boolean?,
        @SerializedName("is_mind_pack_unlocked")
        val isMindPackUnlocked: Boolean?,
        @SerializedName("result")
        val result: String?,
        @SerializedName("fcm_registration_token")
        val fcmToken: String?,
        @SerializedName("email")
        val email:String?
    )

    data class Streak(
        @SerializedName("1")
        val one: String?,
        @SerializedName("2") val two: String?,
        @SerializedName("3") val three: String?,
        @SerializedName("4") val four: String?,
        @SerializedName("5") val five: String?,
        @SerializedName("6") val six: String?,
        @SerializedName("7") val seven: String?
    )
}