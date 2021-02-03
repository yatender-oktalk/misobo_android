package com.example.misohe.myProfile

import android.content.ComponentName
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.misohe.R
import com.example.misohe.talkToExperts.models.CaptureOrderPayload
import com.example.misohe.talkToExperts.models.RatingPayload
import com.example.misohe.talkToExperts.pagination.BookingsExpandedListAdapter
import com.example.misohe.talkToExperts.view.BookASlotDialog
import com.example.misohe.talkToExperts.viewModels.TalkToExpertsViewModel
import com.example.misohe.utils.SharedPreferenceManager
import com.razorpay.PaymentResultListener
import kotlinx.android.synthetic.main.activity_user_bookings.*

class UserBookingsActivity : AppCompatActivity(), PaymentResultListener {

    private val talkToExpertsViewModel: TalkToExpertsViewModel by lazy {
        ViewModelProvider(this).get(TalkToExpertsViewModel::class.java)
    }
    private lateinit var bookingsListAdapter: BookingsExpandedListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_bookings)

        bookingsListAdapter = BookingsExpandedListAdapter({ entry, rating ->
            if (rating != 0) {
                talkToExpertsViewModel.submitRating(
                    RatingPayload(bookingId = entry?.id ?: 0, rating = rating)
                )
            } else {
                talkToExpertsViewModel.selectedExpertLiveDate.postValue(entry?.expert)
                val slotDialog =
                    BookASlotDialog()
                val bundle = Bundle()
                entry?.expert?.id?.let { it1 -> bundle.putInt("ID", it1) }
                slotDialog.arguments = bundle
                supportFragmentManager.beginTransaction()
                    .add(slotDialog, null).commit()
            }
        }, {
            val sendIntent = Intent("android.intent.action.MAIN")
            sendIntent.component = ComponentName("com.whatsapp", "com.whatsapp.Conversation")
            sendIntent.putExtra(
                "jid",
                PhoneNumberUtils.stripSeparators("919809740740") + "@s.whatsapp.net"
            )
            startActivity(sendIntent)
        })

        bookingsRecyclerView.adapter = bookingsListAdapter

        talkToExpertsViewModel.submitRatingLiveData.observe(this, Observer { state ->
            when (state) {
                is FetchState.Success -> {
                    talkToExpertsViewModel.userBookingsLiveData.value?.dataSource?.invalidate()
                }
                is FetchState.Error -> {
                    talkToExpertsViewModel.userBookingsLiveData.value?.dataSource?.invalidate()
                }
            }
        })

        backIcon.setOnClickListener { onBackPressed() }

        talkToExpertsViewModel.getUserBookings(
            SharedPreferenceManager.getUser()?.data?.userId.toString(),
            true
        )
        talkToExpertsViewModel.userBookingsLiveData.observe(this, Observer { state ->
            if (!state.isNullOrEmpty()) {
                bookingsListAdapter.submitList(state)
            }
        })
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Log.i("fail", p0.toString() + " " + p1)
    }

    override fun onPaymentSuccess(p0: String?) {
        Log.i("success", p0.toString())
        talkToExpertsViewModel.captureOrder(
            CaptureOrderPayload(
                p0.toString(),
                "signature",
                talkToExpertsViewModel.currentOrder.value?.data?.orderId ?: 0,
                talkToExpertsViewModel.currentOrder.value?.data?.transactionId ?: 0,
                talkToExpertsViewModel.paymentAmount
            )
        )
    }
}