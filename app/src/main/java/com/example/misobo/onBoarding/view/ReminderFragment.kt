package com.example.misobo.onBoarding.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.misobo.MainActivity
import com.example.misobo.R
import com.example.misobo.utils.Util
import com.example.misobo.onBoarding.viewModels.OnBoardingViewModel
import com.example.misobo.onBoarding.viewModels.ReminderTime
import com.example.misobo.utils.SharedPreferenceManager
import kotlinx.android.synthetic.main.fragment_reminder.*

class ReminderFragment : Fragment() {

    val onBoardingViewModel: OnBoardingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reminder, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setNumberPicker(hourPicker, 0, 12)
        setNumberPicker(minutesPicker, 0, 59)
        setNumberPicker(amPmPicker, 0, 1)
        amPmPicker.displayedValues = arrayOf("AM", "PM")

        reminderBackIcon.setOnClickListener { activity?.onBackPressed() }

        saveReminderButton.setOnClickListener {
            onBoardingViewModel.reminderTime.postValue(
                ReminderTime.SelectedTime(
                    String.format("%02d", hourPicker.value),
                    String.format("%02d", minutesPicker.value),
                    amPmPicker.value
                ) as ReminderTime
            )
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.onBoardingFrameContainer,
                    ReminderSuccessFragment()
                )
                ?.addToBackStack(null)?.commitAllowingStateLoss()
        }

        laterTextView.setOnClickListener {
            SharedPreferenceManager.setOnBoarded(true)
            startActivity(Intent(context, MainActivity::class.java))
            activity?.finish()
        }
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