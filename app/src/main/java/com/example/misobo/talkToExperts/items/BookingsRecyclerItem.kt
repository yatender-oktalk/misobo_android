package com.example.misobo.talkToExperts.items

import android.os.Bundle
import androidx.paging.PagedList
import com.example.misobo.R
import com.example.misobo.talkToExperts.models.ExpertModel
import com.example.misobo.talkToExperts.models.RatingPayload
import com.example.misobo.talkToExperts.models.UserBookings
import com.example.misobo.talkToExperts.pagination.BookingsListAdapter
import com.example.misobo.talkToExperts.view.BookASlotDialog
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.user_bookings_recycler.view.*

class BookingsRecyclerItem(
    val submitRating: (RatingPayload) -> Unit,
    val openSlot: (ExpertModel.Expert?) -> Unit,
    val needHelpClicked: () -> Unit

) : Item() {
    //private lateinit var bookingsListAdapter: BookingsListAdapter
    private lateinit var pagedList: PagedList<UserBookings.Entry?>

    val bookingsListAdapter = BookingsListAdapter({ entry, rating ->
        if (rating != 0) {
            submitRating.invoke(RatingPayload(bookingId = entry?.id ?: 0, rating = rating))
        } else {
            openSlot.invoke(entry?.expert)
        }
    }, { needHelpClicked.invoke() })

    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.bookingsRecyclerView.adapter = bookingsListAdapter
        //bookingsListAdapter.curr
        viewHolder.itemView.bookingsRecyclerView.childCount

        if (this::pagedList.isInitialized) {
            bookingsListAdapter.submitList(pagedList)
        }
    }

    fun update(state: PagedList<UserBookings.Entry?>) {
        pagedList = state
        notifyChanged()
    }

    override fun getLayout(): Int = R.layout.user_bookings_recycler
}