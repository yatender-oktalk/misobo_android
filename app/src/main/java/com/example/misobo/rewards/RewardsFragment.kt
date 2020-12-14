package com.example.misobo.rewards

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.misobo.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_rewards.*

class RewardsFragment : Fragment() {

    private val rewardsViewModel: RewardsViewModel by lazy {
        ViewModelProvider(this).get(RewardsViewModel::class.java)
    }
    val adapter = GroupAdapter<ViewHolder>()
    val section = Section()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rewards, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        rewardsViewModel.getRewards()
        rewardsReyclerView.adapter = adapter

        rewardsViewModel.rewardsLiveData.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is RewardsFetchState.Success -> {
                    section.add(RewardsHeaderItem())
                    section.add(ClaimedRewardsItem())
                    state.rewardsModel.data?.forEach { reward ->
                        section.add(RewardsItem(reward))
                    }
                }
                is RewardsFetchState.Loading -> {
                }
                is RewardsFetchState.Error -> {
                }
            }
        })
        adapter.add(section)
    }

}