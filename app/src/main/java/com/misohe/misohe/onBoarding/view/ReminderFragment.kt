package com.misohe.misohe.onBoarding.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.misohe.misohe.MainActivity
import com.misohe.misohe.R
import com.misohe.misohe.utils.Util
import com.misohe.misohe.onBoarding.viewModels.OnBoardingViewModel
import com.misohe.misohe.onBoarding.viewModels.ReminderTime
import com.misohe.misohe.utils.SharedPreferenceManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_reminder.*

class ReminderFragment : Fragment() {

    private val onBoardingViewModel: OnBoardingViewModel by activityViewModels()

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
            val firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())
            val bundle = Bundle();
            bundle.putString("save_reminder", "");
            firebaseAnalytics.logEvent("reminder", bundle);

            onBoardingViewModel.reminderTime.postValue(
                ReminderTime.SelectedTime(
                    String.format("%02d", hourPicker.value),
                    String.format("%02d", minutesPicker.value),
                    amPmPicker.value
                ) as ReminderTime
            )
            val timeInMinutes =
                ((hourPicker.value + if (amPmPicker.value == 0) 0 else 12) * 60) + minutesPicker.value
            onBoardingViewModel.sendDailyReminder(
                SharedPreferenceManager.getUserProfile()?.data?.id ?: 0,
                JsonObject().apply { addProperty("daily_reminder", timeInMinutes) }
            )
        }

        onBoardingViewModel.startMainActivityTrigger.observe(viewLifecycleOwner, Observer { it ->
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(
                    R.id.onBoardingFrameContainer,
                    ReminderSuccessFragment()
                )
                ?.addToBackStack(null)?.commitAllowingStateLoss()
        })

        laterTextView.setOnClickListener {
            val firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())
            val bundle = Bundle();
            bundle.putString("do_later", "");
            firebaseAnalytics.logEvent("reminder", bundle);

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