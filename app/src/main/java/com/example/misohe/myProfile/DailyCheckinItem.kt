package com.example.misohe.myProfile

import com.bumptech.glide.Glide
import com.example.misohe.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.daily_checking_weekday_item.view.*

class DailyCheckinItem(private val status: String, val day: String) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.weekday_name.text = day

        val image = when (status) {
            "FALSE" -> {
                R.drawable.ic_remove_profile
            }
            "TRUE" -> {
                R.drawable.ic_active
            }
            "TODAY" -> {
                R.drawable.ic_today
            }
            else -> {
                R.drawable.today_circle
            }
        }


        Glide.with(viewHolder.itemView.context).load(image)
            .into(viewHolder.itemView.dailyCheckinStatusImage)
    }

    override fun getLayout(): Int = R.layout.daily_checking_weekday_item
}