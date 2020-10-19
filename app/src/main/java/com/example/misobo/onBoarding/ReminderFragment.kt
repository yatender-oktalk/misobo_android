package com.example.misobo.onBoarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.misobo.R
import com.example.misobo.Util
import kotlinx.android.synthetic.main.fragment_reminder.*

class ReminderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reminder, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setNumberPicker(hourPicker,0,12)
        setNumberPicker(minutesPicker, 0, 59)
        setNumberPicker(amPmPicker,0,1)
        amPmPicker.displayedValues = arrayOf("AM", "PM")
    }

    private fun setNumberPicker(
        picker: NumberPicker,
        minValue: Int,
        maxValue: Int
    ) {
        picker.minValue = minValue
        picker.maxValue = maxValue
        picker.textSize = Util.convertDpToPixels(26F, requireContext()).toFloat()
        picker.textColor = ContextCompat.getColor(requireContext(), R.color.colorAccent)
        picker.selectionDividerHeight = Util.convertDpToPixels(0F, requireContext())
        picker.setFormatter { i -> String.format("%02d", i) }
    }
}