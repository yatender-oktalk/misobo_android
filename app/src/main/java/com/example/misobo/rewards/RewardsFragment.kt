package com.example.misobo.rewards

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.misobo.R

class RewardsFragment : Fragment() {

    private val rewardsViewModel: RewardsViewModel by lazy {
        ViewModelProvider(this).get(RewardsViewModel::class.java)
    }

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

        rewardsViewModel.rewardsLiveData.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is RewardsFetchState.Success -> {
                }
                is RewardsFetchState.Loading -> {
                }
                is RewardsFetchState.Error -> {
                }
            }
        })
    }

}