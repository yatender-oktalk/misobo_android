package com.example.misobo.talkToExperts

import android.text.format.DateFormat
import com.example.misobo.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.time_slots_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class SlotsRecyclerItem(val slotResponse: ExpertSlotsResponse) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val date: Date = dateFormat.parse(slotResponse.date)
        val hours = DateFormat.format("h" , date)
        val minutes = DateFormat.format("mm" , date)
        val amPm = DateFormat.format("a"  , date)
        viewHolder.itemView.slot.text = "${hours}:$minutes $amPm"
    }

    override fun getLayout(): Int = R.layout.time_slots_item
}