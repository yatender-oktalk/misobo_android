package com.example.misobo.talkToExperts.items

import com.example.misobo.R
import com.example.misobo.talkToExperts.models.UserBookings
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.user_bookings_recycler.view.*

class BookingsRecyclerItem(val bookings: UserBookings) : Item() {
    val groupAdapter = GroupAdapter<ViewHolder>()
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.bookingsRecyclerView.adapter = groupAdapter
        groupAdapter.clear()
        val section = Section()
        bookings.data?.entries?.forEach { entry ->
            section.add(UserBookingsItem(entry))
        }
        groupAdapter.add(section)
    }

    override fun getLayout(): Int = R.layout.user_bookings_recycler
}