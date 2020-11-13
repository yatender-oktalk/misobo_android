package com.example.misobo.talkToExperts

import android.text.format.DateFormat
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.misobo.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.slot_book_date_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class DateRecyclerItem(
    val date: String,
    val selectedPosition: Int,
    val onClick: (String, Int) -> Unit
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {

        val format = SimpleDateFormat("yyyy-MM-dd")
        val date: Date = format.parse(date)
        Log.i("Date", "${DateFormat.format("EEEE", date)}")
        val dayOfTheWeek = DateFormat.format("EEEE", date) as String // Thursday
        val day = DateFormat.format("dd", date) as String // 20
        val monthString = DateFormat.format("MMM", date) as String // Jun

        viewHolder.itemView.dayOfWeekTextView.text = dayOfTheWeek
        viewHolder.itemView.dateTextView.text = day
        viewHolder.itemView.monthTextView.text = monthString
        viewHolder.itemView.setOnClickListener { onClick.invoke(this.date, position) }

        if (position == selectedPosition) {
            viewHolder.itemView.root.backgroundTintList = ContextCompat.getColorStateList(
                viewHolder.containerView.context,
                R.color.colorAccent
            )
            viewHolder.itemView.dayOfWeekTextView.setTextColor(
                ContextCompat.getColorStateList(
                    viewHolder.containerView.context,
                    android.R.color.white
                )
            )
            viewHolder.itemView.dateTextView.setTextColor(
                ContextCompat.getColorStateList(
                    viewHolder.containerView.context,
                    android.R.color.white
                )
            )
            viewHolder.itemView.monthTextView.setTextColor(
                ContextCompat.getColorStateList(
                    viewHolder.containerView.context,
                    android.R.color.white
                )
            )
        } else {
            viewHolder.itemView.root.backgroundTintList =
                ContextCompat.getColorStateList(viewHolder.containerView.context, R.color.lightGrey)

            viewHolder.itemView.dayOfWeekTextView.setTextColor(
                ContextCompat.getColorStateList(
                    viewHolder.containerView.context,
                    R.color.darkGrey
                )
            )
            viewHolder.itemView.dateTextView.setTextColor(
                ContextCompat.getColorStateList(
                    viewHolder.containerView.context,
                    R.color.darkGrey
                )
            )
            viewHolder.itemView.monthTextView.setTextColor(
                ContextCompat.getColorStateList(
                    viewHolder.containerView.context,
                    R.color.darkGrey
                )
            )
        }
    }

    override fun getLayout(): Int = R.layout.slot_book_date_item
}