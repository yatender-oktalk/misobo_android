package com.example.misobo.rewards

import com.google.gson.annotations.SerializedName

data class RewardsModel(
    @SerializedName("data")
    val data: List<Reward>?
) {
    data class Reward(
        @SerializedName("company")
        val company: String?,
        @SerializedName("how_to_redeem")
        val howToRedeem: String?,
        @SerializedName("img")
        val img: String?,
        @SerializedName("offer_details")
        val offerDetails: String?,
        @SerializedName("terms_and_conditions")
        val termsAndConditions: String?,
        @SerializedName("title")
        val title: String?,
        @SerializedName("company_logo")
        val companyLogo: String?,
        @SerializedName("karma")
        val karma: Int?,
        @SerializedName("people_unlocked")
        val peopleUnlocked: Long?,
        @SerializedName("is_active")
        val isActive: Boolean?
    )
}