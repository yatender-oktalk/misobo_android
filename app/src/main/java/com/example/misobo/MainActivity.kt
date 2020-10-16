package com.example.misobo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getStartedText.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.onBoardingFrameContainer, MisoboMembersFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

    }
}