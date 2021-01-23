package com.example.misobo.bmi.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.misobo.R
import com.example.misobo.bmi.viewModels.BmiViewModel
import kotlinx.android.synthetic.main.fragment_bmi_full_report.*

class BmiFullReportFragment : Fragment() {
    val bmiViewModel: BmiViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bmi_full_report, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bmiViewModel.bmiDetails.observe(viewLifecycleOwner, Observer { responseBody ->
            bmiValue.text = responseBody.data?.bmi.toString()
            bmiStatus.text = "You are in the ${responseBody.data?.result}  range"
        })

        bmiReportBackIcon.setOnClickListener {
            activity?.onBackPressed()
        }
    }
}