package com.example.misobo.myProfile

import android.content.ComponentName
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.misobo.R
import com.example.misobo.talkToExperts.models.RatingPayload
import com.example.misobo.talkToExperts.pagination.BookingsExpandedListAdapter
import com.example.misobo.talkToExperts.pagination.BookingsListAdapter
import com.example.misobo.talkToExperts.view.BookASlotDialog
import com.example.misobo.talkToExperts.viewModels.TalkToExpertsViewModel
import com.example.misobo.utils.SharedPreferenceManager
import kotlinx.android.synthetic.main.activity_user_bookings.*

class UserBookingsActivity : AppCompatActivity() {

    private val talkToExpertsViewModel: TalkToExpertsViewModel by lazy {
        ViewModelProvider(this).get(TalkToExpertsViewModel::class.java)
    }
    private lateinit var bookingsListAdapter: BookingsExpandedListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_bookings)

        bookingsListAdapter = BookingsExpandedListAdapter({ entry, rating ->
            if (rating != 0) {
                //talkToExpertsViewModel.submitRating(rating)
            } else {
                //talkToExpertsViewModel.selectedExpertLiveDate.postValue(entry)
                val slotDialog =
                    BookASlotDialog()
                val bundle = Bundle()
                entry?.id?.let { it1 -> bundle.putInt("ID", it1) }
                slotDialog.arguments = bundle
                supportFragmentManager.beginTransaction()
                    .add(slotDialog, null).commit()
            }
        }, {
            val sendIntent = Intent("android.intent.action.MAIN")
            sendIntent.component = ComponentName("com.whatsapp", "com.whatsapp.Conversation")
            sendIntent.putExtra(
                "jid",
                PhoneNumberUtils.stripSeparators("+919809740740") + "@s.whatsapp.net"
            )
            startActivity(sendIntent)
        })

        bookingsRecyclerView.adapter = bookingsListAdapter

        talkToExpertsViewModel.getUserBookings(SharedPreferenceManager.getUser()?.data?.userId.toString())
        talkToExpertsViewModel.userBookingsLiveData.observe(this, Observer { state ->
            if (!state.isNullOrEmpty()) {
                bookingsListAdapter.submitList(state)
            }
        })
    }
}