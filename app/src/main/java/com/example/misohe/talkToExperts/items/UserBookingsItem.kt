package com.example.misohe.talkToExperts.items

import com.example.misohe.R
import com.example.misohe.talkToExperts.models.UserBookings
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder

class UserBookingsItem(val entry: UserBookings.Entry) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
      /*  viewHolder.itemView.expertNameTextView.text = entry.expert?.name
        viewHolder.itemView.expertCategory.text = entry.expert?.qualification ?: ""
        viewHolder.itemView.expertLanguage.text = entry.expert?.language
        viewHolder.itemView.coinsNeeded.text = entry.expert?.karmaCoinsNeeded.toString()
        Glide.with(viewHolder.itemView.context).load(entry.expert?.image)
            .placeholder(R.color.colorAccent)
            .into(viewHolder.itemView.expertImages)*/
    }

    override fun getLayout(): Int = R.layout.user_bookings_layout
}