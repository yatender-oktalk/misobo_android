package com.misohe.misohe.onBoarding.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.misohe.misohe.MainActivity
import com.misohe.misohe.R
import com.misohe.misohe.utils.SharedPreferenceManager
import com.misohe.misohe.onBoarding.viewModels.OnBoardingViewModel

class OnBoardingActivity : AppCompatActivity() {

    private val onBoardingViewModel by lazy {
        ViewModelProvider(this).get(OnBoardingViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        if (SharedPreferenceManager.getUserProfile() != null) {
            startMainActivity()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.onBoardingFrameContainer, OnBoardingFragment())
                .commit()
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}