package com.example.misobo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.misobo.onBoarding.MisoboMembersFragment
import com.example.misobo.onBoarding.OnBoardingViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val onBoardingViewModel by lazy {
        ViewModelProvider(this).get(OnBoardingViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getStartedText.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.onBoardingFrameContainer,
                    MisoboMembersFragment()
                )
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }
    }
}