package com.misohe.misohe.talkToExperts.items

import com.misohe.misohe.R
import com.misohe.misohe.utils.SharedPreferenceManager
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.talk_to_experts_coins_layout.view.*

class TalkToExpertsItem(val onBackPressed: () -> Unit) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.backIcon.setOnClickListener { onBackPressed.invoke() }
        viewHolder.itemView.karmaCoinsText.text =
            SharedPreferenceManager.getUserProfile()?.data?.karmaPoints ?: "0"
    }

    override fun getLayout(): Int = R.layout.talk_to_experts_coins_layout
}