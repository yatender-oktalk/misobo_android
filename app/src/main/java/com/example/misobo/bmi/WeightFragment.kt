package com.example.misobo.bmi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.misobo.R
import com.example.misobo.utils.SharedPreferenceManager
import kotlinx.android.synthetic.main.fragment_weight.*

class WeightFragment : Fragment() {
    private val bmiViewModel: BmiViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weight, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        calculateButton.setOnClickListener {
            if (weightEditText.text.isNotEmpty() || weightEditText.text.isNotBlank())
                bmiViewModel.weight.postValue(weightEditText.text.toString().toInt())

            val bmiRequestBody =
                BmiRequestBody(bmiViewModel.height.value ?: 0.0, weightEditText.text.toString().toInt())

            bmiViewModel.saveBmi(
                bmiRequestBody = bmiRequestBody,
                regId = SharedPreferenceManager.getUser()?.data?.registrationId ?: -1
            )
        }
    }


}