package com.example.misobo.talkToExperts.items

import com.example.misobo.R
import com.example.misobo.talkToExperts.models.UserBookings
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.user_bookings_layout.view.*

class UserBookingsItem(val entry: UserBookings.Entry) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.expertNameTextView.text = entry.expert?.name
        viewHolder.itemView.expertCategory.text = "Vedic Astrologer"
        viewHolder.itemView.expertLanguage.text = entry.expert?.language
        viewHolder.itemView.coinsNeeded.text = entry.expert?.karmaCoinsNeeded.toString()
    }

    override fun getLayout(): Int = R.layout.user_bookings_layout
}