package com.example.misobo.myProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.misobo.R
import com.example.misobo.utils.SharedPreferenceManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_my_profile.*


class MyProfileFragment : Fragment() {
    val groupAdapter = GroupAdapter<ViewHolder>()
    private val profileViewModel by lazy { ViewModelProvider(this).get(ProfileViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dailyCheckinRecyclerView.adapter = groupAdapter
        val weekList = listOf<String>("S", "M", "T", "W", "T", "F", "S")
        val section = Section()
        weekList.forEach { section.add(DailyCheckinItem(it)) }
        groupAdapter.add(section)

        profileViewModel.getProfile(SharedPreferenceManager.getUser()?.data?.userId?:-1)
    }
}