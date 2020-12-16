package com.example.misobo.rewards

import com.google.gson.annotations.SerializedName

data class ClaimedRewardsModel(
    @SerializedName("data")
    val data: List<ClaimedReward>?
) {
    data class ClaimedReward(
        @SerializedName("code")
        val code: String?,
        @SerializedName("redeemed_on")
        val redeemedOn: String?,
        @SerializedName("reward")
        val reward: RewardsModel.Reward?,
        @SerializedName("user_id")
        val userId: Int?,
        @SerializedName("valid_from")
        val validFrom: String?,
        @SerializedName("valid_upto")
        val validUpto: String?
    )
}