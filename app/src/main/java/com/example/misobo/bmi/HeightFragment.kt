package com.example.misobo.bmi

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.misobo.R
import com.example.misobo.utils.RuleView
import kotlinx.android.synthetic.main.fragment_height.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HeightFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HeightFragment : Fragment() {

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
                    (ruleView.getCurrentValue() / 12).toString().substringBefore(".")+ruleView.getCurrentValue().toInt() % 12

                Log.i("metres", (footInch.toFloat() * 0.3048).toString())
            }
        })

    }


}