package com.example.misohe.rewards

import com.example.misohe.R
import com.example.misohe.utils.SharedPreferenceManager
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.rewards_header.view.*

class RewardsHeaderItem() : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.karmaCoinsText.text = SharedPreferenceManager.getUserProfile()?.data?.karmaPoints
    }

    override fun getLayout(): Int = R.layout.rewards_header
}