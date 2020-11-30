package com.example.misobo.myProfile

import com.google.gson.annotations.SerializedName

data class ProfileResponseModel(
    @SerializedName("data")
    val data: Data?,
    @SerializedName("msg")
    val message: String?
) {
    data class Data(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("dob")
        val dob: Int?,
        @SerializedName("phone")
        val phone: String?,
        @SerializedName("karma_points")
        val karmaPoints: String?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("registration_id")
        val registrationId: Int?,
        @SerializedName("img")
        val image: String?,
        @SerializedName("weight")
        val weight: Int?,
        @SerializedName("bmi")
        val bmi: String?,
        @SerializedName("height")
        val height: String?,
        @SerializedName("login_streak_days")
        val loginStreakDays: Int?,
        @SerializedName("login_streak")
        val loginStreak: List<Streak>?
    )

    data class Streak(
        val wasActive: Boolean?
    )
}