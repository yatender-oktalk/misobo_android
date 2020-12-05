package com.example.misobo.myProfile

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.misobo.R
import com.example.misobo.utils.SharedPreferenceManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_my_profile.*
import kotlinx.android.synthetic.main.fragment_my_profile.editName

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

        fillName()

        editName.setOnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode === KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                if (!editName.text.isNullOrEmpty()) {
                    SharedPreferenceManager.setName(editName.text.toString())
                    fillName()
                }
            }
            false
        }

        profileViewModel.getProfile(SharedPreferenceManager.getUser()?.data?.userId ?: -1)


        profileViewModel.profileLiveData.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is ProfileResponseAction.Success -> {
                    SharedPreferenceManager.setUserProfile(state.response)
                    groupAdapter.clear()
                    val section = Section()

                    section.add(
                        DailyCheckinItem(
                            state.response.data?.loginStreak?.one ?: false,
                            "S"
                        )
                    )
                    section.add(
                        DailyCheckinItem(
                            state.response.data?.loginStreak?.two ?: false,
                            "M"
                        )
                    )
                    section.add(
                        DailyCheckinItem(
                            state.response.data?.loginStreak?.three ?: false,
                            "T"
                        )
                    )
                    section.add(
                        DailyCheckinItem(
                            state.response.data?.loginStreak?.four ?: false,
                            "W"
                        )
                    )
                    section.add(
                        DailyCheckinItem(
                            state.response.data?.loginStreak?.five ?: false,
                            "T"
                        )
                    )
                    section.add(
                        DailyCheckinItem(
                            state.response.data?.loginStreak?.six ?: false,
                            "F"
                        )
                    )
                    section.add(
                        DailyCheckinItem(
                            state.response.data?.loginStreak?.seven ?: false,
                            "S"
                        )
                    )

                    groupAdapter.add(section)
                    Toast.makeText(
                        context,
                        state.response.data?.loginStreak?.two.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is ProfileResponseAction.Loading -> {

                }
                is ProfileResponseAction.Error -> {
                    Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
                }
            }
        })

    }

    private fun fillName() {
        if (SharedPreferenceManager.getName() != null) {
            editNameGroup.visibility = View.INVISIBLE
            nameTextView.text = SharedPreferenceManager.getName()
            nameTextView.visibility = View.VISIBLE
        } else {
            nameTextView.visibility = View.INVISIBLE
            editNameGroup.visibility = View.VISIBLE
        }
    }
}