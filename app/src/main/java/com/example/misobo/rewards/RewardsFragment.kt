package com.example.misobo.rewards

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.misobo.R
import com.example.misobo.utils.SharedPreferenceManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_rewards.*

class RewardsFragment : Fragment() {

    private val rewardsViewModel: RewardsViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rewards, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = GroupAdapter<ViewHolder>()
        val rewardsHeaderSection = Section()
        val claimedRewardsSection = Section()
        val rewardsSection = Section()

        if (rewardsViewModel.rewardsLiveData.value == null)
            rewardsViewModel.getRewards()

        if (rewardsViewModel.claimedRewardsLiveData.value == null)
            rewardsViewModel.claimedRewards(SharedPreferenceManager.getUser()?.data?.userId ?: -1)

        rewardsReyclerView.adapter = adapter
        rewardsHeaderSection.add(RewardsHeaderItem())
        rewardsViewModel.rewardsLiveData.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is RewardsFetchState.Success -> {
                    state.rewardsModel.data?.forEach { reward ->
                        rewardsSection.add(RewardsItem(reward) {
                            rewardsViewModel.selectedRewardLiveData.postValue(reward)
                            activity?.supportFragmentManager?.beginTransaction()
                                ?.add(RewardsDetailsBottomSheet(), null)?.commit()
                        })
                    }
                }
            }
        })

        rewardsViewModel.claimedRewardsLiveData.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is ClaimedRewardsFetchState.Success -> {
                    claimedRewardsSection.setHeader(
                        ClaimedRewardsItem(
                            state.claimedRewardsModel.data?.size ?: 0
                        ) {
                            rewardsViewModel.claimedRewardsList.postValue(state.claimedRewardsModel.data)
                            activity?.supportFragmentManager?.beginTransaction()
                                ?.replace(R.id.mainContainer, ClaimedRewardsFragment())
                                ?.addToBackStack(null)?.commit()
                        }
                    )
                }
            }
        })
        adapter.add(rewardsHeaderSection)
        adapter.add(claimedRewardsSection)
        adapter.add(rewardsSection)
    }
}