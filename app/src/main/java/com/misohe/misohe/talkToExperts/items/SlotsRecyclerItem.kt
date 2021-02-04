package com.misohe.misohe.talkToExperts.items

import android.text.format.DateFormat
import androidx.core.content.ContextCompat
import com.misohe.misohe.R
import com.misohe.misohe.talkToExperts.models.ExpertSlotsResponse
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.time_slots_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class SlotsRecyclerItem(
    val slotResponse: ExpertSlotsResponse,
    val selectedPosition: Int,
    val onClick: (Long?,Int) -> Unit
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val date: Date = dateFormat.parse(slotResponse.date)
        val hours = DateFormat.format("h", date)
        val minutes = DateFormat.format("mm", date)
        val amPm = DateFormat.format("a", date)
        viewHolder.itemView.slot.text = "${hours}:$minutes $amPm"

        viewHolder.itemView.setOnClickListener {
            onClick.invoke(slotResponse.unixTime,position)
        }


        if (position == selectedPosition) {
            viewHolder.itemView.slot.backgroundTintList = ContextCompat.getColorStateList(
                viewHolder.containerView.context,
                R.color.colorAccent
            )
            viewHolder.itemView.slot.setTextColor(
                ContextCompat.getColorStateList(
                    viewHolder.containerView.context,
                    android.R.color.white
                )
            )
        } else {
            viewHolder.itemView.slot.backgroundTintList =
                ContextCompat.getColorStateList(viewHolder.containerView.context, R.color.lightGrey)

            viewHolder.itemView.slot.setTextColor(
                ContextCompat.getColorStateList(
                    viewHolder.containerView.context,
                    R.color.colorAccent
                )
            )
        }
    }

    override fun getLayout(): Int = R.layout.time_slots_item

}


