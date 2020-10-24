package com.example.misobo.onBoarding.view

import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.misobo.R
import com.example.misobo.SharedPreferenceManager
import com.example.misobo.onBoarding.viewModels.OnBoardingViewModel
import com.example.misobo.onBoarding.models.RegistrationModel
import kotlinx.android.synthetic.main.activity_onboarding.*

class OnBoardingActivity : AppCompatActivity() {

    private val onBoardingViewModel by lazy {
        ViewModelProvider(this).get(OnBoardingViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        onBoardingViewModel.userLiveData.observe(this, Observer { user ->
            SharedPreferenceManager.setUser(this, user)
        })

        try {
            val registrationModel =
                RegistrationModel(
                    Settings.Secure.getString(
                        this.contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                )
            if (SharedPreferenceManager.getUser(this) == null) {
                onBoardingViewModel.registerUser(
                    registrationModel
                )
            }
        }catch (e:Exception){
            Toast.makeText(this,e.localizedMessage,Toast.LENGTH_SHORT).show()
        }

        getStartedText.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.onBoardingFrameContainer,
                    MisoboMembersFragment()
                )
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }
    }
}