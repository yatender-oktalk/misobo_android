package com.example.misobo.talkToExperts.view

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.misobo.R
import com.example.misobo.myProfile.FetchState
import com.example.misobo.talkToExperts.items.BookingsRecyclerItem
import com.example.misobo.talkToExperts.items.ExpertsViewPagerItem
import com.example.misobo.talkToExperts.items.TalkToExpertsItem
import com.example.misobo.talkToExperts.models.RatingPayload
import com.example.misobo.talkToExperts.pagination.BookingsListAdapter
import com.example.misobo.talkToExperts.viewModels.CategoriesState
import com.example.misobo.talkToExperts.viewModels.TalkToExpertsViewModel
import com.example.misobo.utils.SharedPreferenceManager
import com.example.misobo.utils.ToggleGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_talkto_experts_home.*


class TalktoExpertsHomeFragment : Fragment() {

    private val viewModel: TalkToExpertsViewModel by activityViewModels()
    private val groupAdapter = GroupAdapter<ViewHolder>()
    private lateinit var bookingsListAdapter: BookingsListAdapter
    private lateinit var userBookingsSection: BookingsRecyclerItem
    private lateinit var expertsViewPagerItem: ExpertsViewPagerItem
    private lateinit var userBookingsToggle: ToggleGroup

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_talkto_experts_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //groupAdapter.clear()
        talkToExpertsRecyclerView.adapter = groupAdapter
        val coinsSection = Section()

        coinsSection.add(TalkToExpertsItem() { activity?.onBackPressed() })

        viewModel.getCoinsLiveData()
            .observe(viewLifecycleOwner,
                Observer { response ->
                    viewModel.userBookingsLiveData.value?.dataSource?.invalidate()
                    coinsSection.notifyChanged()
                })

        viewModel.getExpertCategories()
        viewModel.getUserBookings(SharedPreferenceManager.getUser()?.data?.userId.toString())

        //backIcon.setOnClickListener { activity?.onBackPressed() }

        bookingsListAdapter = BookingsListAdapter({ entry, rating ->
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
        }, {
        })

        //User Bookings
        userBookingsSection = BookingsRecyclerItem({ ratingPayload ->
            viewModel.submitRating(ratingPayload)
        }, { expert ->
            viewModel.selectedExpertLiveDate.postValue(expert)
            val slotDialog =
                BookASlotDialog()
            val bundle = Bundle()
            expert?.id?.let { it1 -> bundle.putInt("ID", it1) }
            slotDialog.arguments = bundle
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(slotDialog, null)?.commit()
        }, {
            val sendIntent = Intent("android.intent.action.MAIN")
            sendIntent.component = ComponentName("com.whatsapp", "com.whatsapp.Conversation")
            sendIntent.putExtra(
                "jid",
                PhoneNumberUtils.stripSeparators("+919809740740") + "@s.whatsapp.net"
            )
            context?.startActivity(sendIntent)
        })

        userBookingsToggle = ToggleGroup()
        userBookingsToggle.add(userBookingsSection)
        viewModel.userBookingsLiveData.observe(viewLifecycleOwner, Observer { state ->
            if (!state.isNullOrEmpty()) {
                userBookingsSection.update(state)
                userBookingsToggle.show()
                Log.i("stateSize", userBookingsSection.bookingsListAdapter.itemCount.toString())
                Log.i(
                    "stateSize2",
                    userBookingsSection.bookingsListAdapter.currentList?.size.toString()
                )
            } else {
                userBookingsToggle.hide()
            }
        })

        viewModel.submitRatingLiveData.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is FetchState.Success -> {
                    viewModel.userBookingsLiveData.value?.dataSource?.invalidate()
                }
                is FetchState.Error -> {
                    viewModel.userBookingsLiveData.value?.dataSource?.invalidate()
                }
            }
        })

        val expertsSection = Section()


        viewModel.categoriesExpertLiveData.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is CategoriesState.Success -> {
                    expertsViewPagerItem = ExpertsViewPagerItem(this, state)
                    expertsSection.add(expertsViewPagerItem)
                    expertsViewPagerItem.update()
                }

                is CategoriesState.Fail -> {

                }
                is CategoriesState.Loading -> {

                }
            }
        })

        groupAdapter.apply {
            add(coinsSection)
            add(userBookingsToggle)
            add(expertsSection)
        }
    }
}