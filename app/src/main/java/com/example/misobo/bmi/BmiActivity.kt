package com.example.misobo.bmi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.misobo.R
import com.example.misobo.onBoarding.viewModels.OnBoardingViewModel

class BmiActivity : AppCompatActivity() {

    private val bmiViewModel by lazy {
        ViewModelProvider(this).get(BmiViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi)

        supportFragmentManager.beginTransaction()
            .replace(R.id.bmiFrameContainer, BmiHomeFragment())
            .commit()

        bmiViewModel.bmiActionLiveData.observe(this, Observer {
            when (it) {
                is BmiViewModel.BmiResponseAction.Success -> {
                    bmiViewModel.bmiDetails.postValue(it.bmiResponsebody)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.bmiFrameContainer, BmiScoreFragment())
                        .commit()
                }

                is BmiViewModel.BmiResponseAction.Loading -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.bmiFrameContainer, BmiLoadingFragment())
                        .addToBackStack(null)
                        .commit()
                }

                is BmiViewModel.BmiResponseAction.Error -> {

                }
            }
        })
    }
}