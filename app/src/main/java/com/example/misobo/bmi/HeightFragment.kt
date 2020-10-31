package com.example.misobo.bmi

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.misobo.R
import com.example.misobo.onBoarding.viewModels.OnBoardingViewModel
import com.example.misobo.utils.RuleView
import kotlinx.android.synthetic.main.fragment_height.*

class HeightFragment : Fragment() {

    val bmiViewModel: BmiViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_height, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        feetValue.text = (ruleView.getCurrentValue() / 12).toString().substringBefore(".")
        inchValue.text = "${ruleView.getCurrentValue().toInt() % 12}"

        ruleView.setOnValueChangedListener(object : RuleView.OnValueChangedListener {
            override fun onValueChanged(value: Float) {
                feetValue.text = (ruleView.getCurrentValue() / 12).toString().substringBefore(".")
                inchValue.text = "${ruleView.getCurrentValue().toInt() % 12}"
                val footInch =
                    (ruleView.getCurrentValue() / 12).toString()
                        .substringBefore(".") + ruleView.getCurrentValue().toInt() % 12

            }
        })

        nextButton.setOnClickListener {
            val inchToM = inchValue.text.toString().toDouble() * 0.0254
            val footToM = feetValue.text.toString().toDouble() * 0.3048
            val heightInMetres = inchToM + footToM

            bmiViewModel.height.postValue(heightInMetres)
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.bmiFrameContainer, WeightFragment())
                ?.addToBackStack(null)?.commit()
        }

    }


}