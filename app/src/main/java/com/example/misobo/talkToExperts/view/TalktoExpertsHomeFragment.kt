package com.example.misobo.talkToExperts.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.misobo.R
import com.example.misobo.myProfile.FetchState
import com.example.misobo.talkToExperts.items.SubmitRatingItems
import com.example.misobo.talkToExperts.items.UserBookingsItem
import com.example.misobo.talkToExperts.viewModels.CategoriesState
import com.example.misobo.talkToExperts.viewModels.TalkToExpertsViewModel
import com.example.misobo.talkToExperts.models.ExpertCategoriesModel
import com.example.misobo.talkToExperts.models.RatingPayload
import com.example.misobo.talkToExperts.viewModels.UserBookingsFetchState
import com.example.misobo.utils.SharedPreferenceManager
import com.example.misobo.utils.Util
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_talkto_experts_home.*
import java.text.SimpleDateFormat
import java.util.*

class TalktoExpertsHomeFragment : Fragment() {

    private val viewModel: TalkToExpertsViewModel by activityViewModels()
    private val groupAdapter = GroupAdapter<ViewHolder>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_talkto_experts_home, container, false)
    }

    override fun onResume() {
        super.onResume()
        karmaCoinsText.text = SharedPreferenceManager.getUserProfile()?.data?.karmaPoints ?: "0"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.getCoinsLiveData()
            .observe(viewLifecycleOwner,
                Observer { response ->
                    karmaCoinsText.text = response.data?.karmaPoints ?: "0"
                    Log.i("profile", response.toString())
                })

        viewModel.getExpertCategories()
        bookingsRecyclerView.adapter = groupAdapter


        backIcon.setOnClickListener { activity?.onBackPressed() }

        viewModel.getUserBookings(SharedPreferenceManager.getUser()?.data?.userId.toString())
        viewModel.userBookingsLiveData.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is UserBookingsFetchState.Success -> {
                    if (!state.userBookings.data?.entries.isNullOrEmpty()) {
                        currentBookingsGroup.visibility = View.VISIBLE
                        groupAdapter.clear()
                        val section = Section()
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                        val currentDateTime = Date().time
                        Log.i("date", Date().time.toString())
                        state.userBookings.data?.entries?.forEach {
                            val callCompleted =
                                currentDateTime.compareTo(dateFormat.parse(it.endTime).time)
                            if (callCompleted == 1 && it.isRated == false) {
                                section.add(SubmitRatingItems(it) { rating ->
                                    if (rating != 0) {
                                        viewModel.submitRating(
                                            RatingPayload(bookingId = it.id ?: 0, rating = rating)
                                        )
                                    } else {
                                        viewModel.selectedExpertLiveDate.postValue(it.expert)
                                        val slotDialog =
                                            BookASlotDialog()
                                        val bundle = Bundle()
                                        it.id?.let { it1 -> bundle.putInt("ID", it1) }
                                        slotDialog.arguments = bundle
                                        activity?.supportFragmentManager?.beginTransaction()
                                            ?.add(slotDialog, null)?.commit()
                                    }
                                })
                            } else if (callCompleted != 1 && it.isRated == false) {
                                section.add(UserBookingsItem(it))
                            }
                            //Log.i("comp", currentDateTime.compareTo(dateFormat.parse(it.endTime).time).toString())
                        }
                        groupAdapter.add(section)
                    } else {
                        currentBookingsGroup.visibility = View.GONE
                    }
                }
                is UserBookingsFetchState.Fail -> {
                    currentBookingsGroup.visibility = View.GONE
                }
            }
        })

        viewModel.submitRatingLiveData.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is FetchState.Success -> {
                    viewModel.getUserBookings(SharedPreferenceManager.getUser()?.data?.userId.toString())
                }
                is FetchState.Error -> {
                    viewModel.getUserBookings(SharedPreferenceManager.getUser()?.data?.userId.toString())
                }
            }
        })

        viewModel.categoriesExpertLiveData.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is CategoriesState.Success -> {
                    val expertList = mutableListOf(
                        ExpertCategoriesModel(
                            -1,
                            "All"
                        )
                    )
                    expertList.addAll(state.categoriesModel)
                    expertViewPager.adapter =
                        ExpertPagerAdapter(
                            requireActivity().supportFragmentManager,
                            expertList
                        )
                    tabLayout.setupWithViewPager(expertViewPager)
                    tabLayout.tabRippleColor = null

                    for (i in 0 until tabLayout.tabCount) {
                        val tab = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
                        val p = tab.layoutParams as MarginLayoutParams
                        if (i == 0) p.setMargins(
                            Util.convertDpToPixels(16F, requireContext()),
                            0,
                            Util.convertDpToPixels(8F, requireContext()),
                            0
                        )
                        else p.setMargins(0, 0, Util.convertDpToPixels(8F, requireContext()), 0)
                        tab.requestLayout()
                    }
                }

                is CategoriesState.Fail -> {

                }
                is CategoriesState.Loading -> {

                }
            }
        })


    }
}