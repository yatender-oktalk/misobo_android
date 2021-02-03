package com.example.misohe.onBoarding.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.misohe.R
import kotlinx.android.synthetic.main.fragment_misobo_members.*

class MisoboMembersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_misobo_members, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        backIcon.setOnClickListener {
            activity?.onBackPressed()
        }

        continueButton.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(
                    R.id.onBoardingFrameContainer,
                    MisoboCategoriesFragment()
                )
                ?.addToBackStack(null)
                ?.commitAllowingStateLoss()
        }
    }
}