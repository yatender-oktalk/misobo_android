package com.example.misobo.bmi.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.misobo.R
import com.example.misobo.bmi.viewModels.BmiViewModel
import kotlinx.android.synthetic.main.fragment_gender.*

class GenderFragment : Fragment() {

    val bmiViewModel: BmiViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gender, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        maleIcon.setOnClickListener { redirectToHeight("M") }
        femaleIcon.setOnClickListener { redirectToHeight("F") }
        backIcon.setOnClickListener { activity?.onBackPressed() }
    }

    private fun redirectToHeight(gender: String) {
        bmiViewModel.gender.postValue(gender)
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(
                R.id.bmiFrameContainer,
                HeightFragment()
            )
            ?.addToBackStack(null)?.commit()
    }

}