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
import androidx.preference.PreferenceManager
import com.example.misobo.Misobo
import com.example.misobo.R
import com.example.misobo.talkToExperts.items.BookingsRecyclerItem
import com.example.misobo.talkToExperts.items.UserBookingsItem
import com.example.misobo.talkToExperts.viewModels.CategoriesState
import com.example.misobo.talkToExperts.viewModels.TalkToExpertsViewModel
import com.example.misobo.talkToExperts.models.ExpertCategoriesModel
import com.example.misobo.talkToExperts.viewModels.UserBookingsFetchState
import com.example.misobo.utils.LiveSharedPreference
import com.example.misobo.utils.SharedPreferenceManager
import com.example.misobo.utils.Util
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_talkto_experts_home.*

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
                    karmaCoinsText.text = response.data?.karmaPoints?:"0"
                    Log.i("profile" , response.toString())
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
                        state.userBookings.data?.entries?.forEach {
                            section.add(UserBookingsItem(it))
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