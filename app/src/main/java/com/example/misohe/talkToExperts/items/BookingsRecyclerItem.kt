package com.example.misohe.talkToExperts.items

import androidx.paging.PagedList
import com.example.misohe.R
import com.example.misohe.talkToExperts.models.ExpertModel
import com.example.misohe.talkToExperts.models.RatingPayload
import com.example.misohe.talkToExperts.models.UserBookings
import com.example.misohe.talkToExperts.pagination.BookingsListAdapter
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.user_bookings_recycler.view.*

class BookingsRecyclerItem(
    private val submitRating: (RatingPayload) -> Unit,
    private val openSlot: (ExpertModel.Expert?) -> Unit,
    private val needHelpClicked: () -> Unit

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

    fun update(list: PagedList<UserBookings.Entry?>) {
        pagedList = list
        notifyChanged()
    }

    override fun getLayout(): Int = R.layout.user_bookings_recycler
}