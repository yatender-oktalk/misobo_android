package com.example.misobo.rewards

import com.example.misobo.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder

class ClaimedRewardsItem() : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
    }

    override fun getLayout(): Int = R.layout.claimed_rewards_layout
}