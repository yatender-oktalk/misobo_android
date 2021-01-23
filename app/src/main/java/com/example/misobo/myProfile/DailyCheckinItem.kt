package com.example.misobo.myProfile

import com.bumptech.glide.Glide
import com.example.misobo.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.daily_checking_weekday_item.view.*

class DailyCheckinItem(private val wasActive: Boolean, val day: String) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.weekday_name.text = day
        if (wasActive) {
            Glide.with(viewHolder.itemView.context).load(R.drawable.ic_active).into(viewHolder.itemView.dailyCheckinStatusImage)
        } else {
            Glide.with(viewHolder.itemView.context).load(R.drawable.ic_remove_profile).into(viewHolder.itemView.dailyCheckinStatusImage)
        }
    }

    override fun getLayout(): Int = R.layout.daily_checking_weekday_item
}