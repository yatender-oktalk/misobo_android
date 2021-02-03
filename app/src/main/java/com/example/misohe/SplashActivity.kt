package com.example.misohe

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.misohe.onBoarding.view.OnBoardingActivity
import com.google.firebase.analytics.FirebaseAnalytics

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed(Runnable { /* Create an Intent that will start the MainActivity. */
            val mainIntent = Intent(this@SplashActivity, OnBoardingActivity::class.java)
            startActivity(mainIntent)
            finish()
        }, 3000)

        val firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        firebaseAnalytics.logEvent("app_open", null);
    }
}