package com.example.misobo.myProfile

import android.view.animation.AnimationUtils
import com.example.misobo.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.daily_checking_weekday_item.view.*

class DailyCheckinItem(private val weekDay: String) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.weekday_name.text = weekDay
    }

    override fun getLayout(): Int = R.layout.daily_checking_weekday_item
}