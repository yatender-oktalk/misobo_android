package com.example.misobo.rewards

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.misobo.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_claimed_rewards.*

class ClaimedRewardsFragment : Fragment() {

    private val rewardsViewModel: RewardsViewModel by activityViewModels()
    private val groupedAdapter = GroupAdapter<ViewHolder>()
    private val section = Section()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_claimed_rewards, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.bottomNavGroup?.visibility = View.GONE
        activity?.arcSeparator?.visibility = View.GONE
        activity?.arc?.visibility = View.GONE

        recyclerView.adapter = groupedAdapter
        rewardsViewModel.claimedRewardsList.observe(viewLifecycleOwner, Observer { rewardsList ->
            rewardsList.forEach {
                section.add(RewardsItem(it.reward!!, it.code.toString()) {
                    rewardsViewModel.selectedRewardLiveData.postValue(it.reward)
                    val bundle = Bundle()
                    bundle.putBoolean("hideButton", true)
                    val rewardsBottomSheet = RewardsDetailsBottomSheet()
                    rewardsBottomSheet.arguments = bundle
                    rewardsBottomSheet.show(activity?.supportFragmentManager!!,null)
                    /*activity?.supportFragmentManager?.beginTransaction()
                        ?.add(rewardsBottomSheet, null)?.commitAllowingStateLoss()*/
                })
            }
        })
        backIcon.setOnClickListener { activity?.onBackPressed() }
        groupedAdapter.add(section)
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.bottomNavGroup?.visibility = View.VISIBLE
        activity?.arcSeparator?.visibility = View.VISIBLE
        activity?.arc?.visibility = View.VISIBLE
    }
}