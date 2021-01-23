package com.example.misobo.bmi.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.misobo.MainActivity
import com.example.misobo.R
import com.example.misobo.bmi.viewModels.BmiViewModel
import kotlinx.android.synthetic.main.fragment_bmi_full_report.*

class BmiFullReportFragment : Fragment() {

    private val bmi by lazy { requireArguments().getDouble("BMI", 0.0) }
    private val result by lazy { requireArguments().getString("RESULT", "") }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bmi_full_report, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bmiValue.text = bmi.toString()
        bmiStatus.text = "You are in the $result  range"

        calculateBmiButton.setOnClickListener {
            startActivity(Intent(requireContext(), BmiActivity::class.java))
            activity?.finish()
        }

        startYourFitnessJourney.setOnClickListener {
            startActivity(Intent(requireContext(), MainActivity::class.java))
            activity?.finish()
        }

        bmiReportBackIcon.setOnClickListener {
            activity?.onBackPressed()
        }
    }
}