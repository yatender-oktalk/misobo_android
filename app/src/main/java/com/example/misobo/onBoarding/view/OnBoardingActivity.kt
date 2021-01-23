package com.example.misobo.onBoarding.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.misobo.MainActivity
import com.example.misobo.R
import com.example.misobo.utils.SharedPreferenceManager
import com.example.misobo.onBoarding.viewModels.OnBoardingViewModel

class OnBoardingActivity : AppCompatActivity() {

    private val onBoardingViewModel by lazy {
        ViewModelProvider(this).get(OnBoardingViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        if (SharedPreferenceManager.getUser() != null && SharedPreferenceManager.isOnBoarded()) {
            startMainActivity()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.onBoardingFrameContainer, OnBoardingFragment())
                .commit()
        }
        onBoardingViewModel.startMainActivityTrigger.observe(this, Observer {
            startMainActivity()
        })
    }

    private fun startMainActivity(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}