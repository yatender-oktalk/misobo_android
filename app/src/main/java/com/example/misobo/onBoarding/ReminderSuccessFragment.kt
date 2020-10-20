package com.example.misobo.onBoarding

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.misobo.R
import kotlinx.android.synthetic.main.fragment_reminder_success.*

class ReminderSuccessFragment : Fragment() {
    val onBoardingViewModel: OnBoardingViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reminder_success, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        onBoardingViewModel.reminderTime.observe(viewLifecycleOwner, Observer { time ->
            when (time) {
                is ReminderTime.SelectedTime -> timeTextView.text =
                    "${time.hour}:${time.minutes}${if (time.amPm == 0) "am" else "pm"}"
            }
        })

        progress.max = 1000
        val currentProgress = 1000
        ObjectAnimator.ofInt(progress, "progress", currentProgress)
            .setDuration(3000)
            .start()
    }
}