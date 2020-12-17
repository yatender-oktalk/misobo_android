package com.example.misobo.onBoarding.view

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.misobo.MainActivity
import com.example.misobo.R
import com.example.misobo.onBoarding.viewModels.OnBoardingViewModel
import com.example.misobo.onBoarding.viewModels.ReminderTime
import com.example.misobo.utils.SharedPreferenceManager
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

        progressBar.max = 1000
        val currentProgress = 1000
        val anim = ObjectAnimator.ofInt(progressBar, "progress", currentProgress)
        anim.setDuration(3000)
        anim.doOnEnd {
            SharedPreferenceManager.setOnBoarded(true)
            //onBoardingViewModel.startMainActivityTrigger.postValue(true)
            startActivity(Intent(context, MainActivity::class.java))
            activity?.finish()
        }
        anim.start()


    }
}