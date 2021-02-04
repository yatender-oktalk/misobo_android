package com.misohe.misohe.rewards

import com.misohe.misohe.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.claimed_rewards_layout.view.*

class ClaimedRewardsItem(val size: Int, val onClick: () -> Unit) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.claimedRewardsCount.text = size.toString()
        viewHolder.itemView.setOnClickListener { onClick.invoke() }
    }

    override fun getLayout(): Int = R.layout.claimed_rewards_layout
}