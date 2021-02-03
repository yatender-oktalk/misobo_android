package com.example.misohe.onBoarding.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.misohe.MainActivity
import com.example.misohe.R
import com.example.misohe.onBoarding.viewModels.OnBoardingViewModel

class MisoboMembersActivity : AppCompatActivity() {

    private val onBoardingViewModel by lazy {
        ViewModelProvider(this).get(OnBoardingViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_misobo_members)

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.onBoardingFrameContainer,
                MisoboMembersFragment()
            ).commitAllowingStateLoss()

    }

    private fun startMainActivity(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}