package com.example.misobo

import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.misobo.onBoarding.MisoboMembersFragment
import com.example.misobo.onBoarding.OnBoardingViewModel
import com.example.misobo.onBoarding.RegistrationModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val onBoardingViewModel by lazy {
        ViewModelProvider(this).get(OnBoardingViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        onBoardingViewModel.userLiveData.observe(this, Observer { user ->
            SharedPreferenceManager.setUser(this, user)
        })

        try {
            val registrationModel = RegistrationModel(
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