package com.misohe.misohe.talkToExperts.models

import com.google.gson.annotations.SerializedName

data class Packs(
    @SerializedName("id")
    val id:Int?,
    @SerializedName("is_enabled")
    val isEnabled:Boolean?,
    @SerializedName("amount")
    val amount:String?,
    @SerializedName("karma_coins")
    val karmaCoins:Int?,
    @SerializedName("tag")
    val tag:String?

) {
}