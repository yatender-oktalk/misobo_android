package com.example.misobo.rewards

import com.bumptech.glide.Glide
import com.example.misobo.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.offer_layout.view.*

class RewardsItem(
    val reward: RewardsModel.Reward,
    val code: String? = null,
    val onClick: () -> Unit
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        val context = viewHolder.containerView.context
        Glide.with(context).load(reward.img).into(viewHolder.itemView.imageView)
        viewHolder.itemView.coinsTextView.text = reward.karma.toString()
        viewHolder.itemView.setOnClickListener { onClick.invoke() }
        viewHolder.itemView.getThisRewardTextView.text =
            if (code.isNullOrEmpty()) "Get this reward" else code
    }

    override fun getLayout(): Int = R.layout.offer_layout
}