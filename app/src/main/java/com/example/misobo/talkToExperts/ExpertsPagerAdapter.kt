package com.example.misobo.talkToExperts

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ExpertsPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ExpertsFragment()
            1 -> ExpertsFragment()
            2 -> ExpertsFragment()
            3 -> ExpertsFragment()
            4 -> ExpertsFragment()
            5 -> ExpertsFragment()
            6 -> ExpertsFragment()
            else -> ExpertsFragment()
        }
    }
}