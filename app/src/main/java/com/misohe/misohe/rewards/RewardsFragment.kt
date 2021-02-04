package com.misohe.misohe.rewards

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.misohe.misohe.R
import com.misohe.misohe.myProfile.ProfileViewModel
import com.misohe.misohe.utils.SharedPreferenceManager
import com.google.android.material.snackbar.Snackbar
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_rewards.*

class RewardsFragment : Fragment() {

    private val rewardsViewModel: RewardsViewModel by activityViewModels()
    val profileViewModel: ProfileViewModel by lazy { ViewModelProvider(this).get(ProfileViewModel::class.java) }

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

        rewardsViewModel.claimedRewards(SharedPreferenceManager.getUser()?.data?.userId ?: -1)

        profileViewModel.getProfileLiveData().observe(viewLifecycleOwner, Observer { profile ->
            rewardsHeaderSection.setHeader(RewardsHeaderItem())
        })
        rewardsReyclerView.adapter = adapter

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

        rewardsViewModel.snackBarLiveData.observe(viewLifecycleOwner, Observer {
            rewardsViewModel.claimedRewards(SharedPreferenceManager.getUser()?.data?.userId ?: -1)
            showSnackBar()
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
                                ?.add(R.id.mainContainer, ClaimedRewardsFragment())
                                ?.addToBackStack(null)?.commitAllowingStateLoss()

                        }
                    )
                }
            }
        })
        adapter.add(rewardsHeaderSection)
        adapter.add(claimedRewardsSection)
        adapter.add(rewardsSection)
    }

    fun showSnackBar() {
        val snackBar: Snackbar =
            Snackbar.make(topLevelView, "Reward claimed successfully", Snackbar.LENGTH_SHORT)
        val snackBarLayout = snackBar.view
        val textView =
            snackBarLayout.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_outlined_tick, 0, 0, 0)
        textView.compoundDrawablePadding =
            resources.getDimensionPixelOffset(R.dimen.snackbar_icon_padding)
        snackBar.show()
    }
}