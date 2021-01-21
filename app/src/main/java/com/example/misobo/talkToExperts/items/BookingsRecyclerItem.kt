package com.example.misobo.talkToExperts.items

import android.os.Bundle
import androidx.paging.PagedList
import com.example.misobo.R
import com.example.misobo.talkToExperts.models.RatingPayload
import com.example.misobo.talkToExperts.models.UserBookings
import com.example.misobo.talkToExperts.pagination.BookingsListAdapter
import com.example.misobo.talkToExperts.view.BookASlotDialog
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_talkto_experts_home.*
import kotlinx.android.synthetic.main.user_bookings_recycler.view.*

class BookingsRecyclerItem(val pagedList: PagedList<UserBookings.Entry>) : Item() {
    private lateinit var bookingsListAdapter: BookingsListAdapter

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.bookingsRecyclerView.adapter = bookingsListAdapter

        bookingsListAdapter = BookingsListAdapter { entry, rating ->
            if (rating != 0) {
                viewModel.submitRating(
                    RatingPayload(bookingId = entry?.id ?: 0, rating = rating)
                )
            } else {
                viewModel.selectedExpertLiveDate.postValue(entry?.expert)
                val slotDialog =
                    BookASlotDialog()
                val bundle = Bundle()
                entry?.expert?.id?.let { it1 -> bundle.putInt("ID", it1) }
                slotDialog.arguments = bundle
                activity?.supportFragmentManager?.beginTransaction()
                    ?.add(slotDialog, null)?.commit()
            }
        }

        bookingsListAdapter.submitList(pagedList)
    }

    override fun getLayout(): Int = R.layout.user_bookings_recycler
}