package com.example.misobo.talkToExperts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.misobo.R

class TalkToExpertActivity : AppCompatActivity() {

    val viewModel: TalkToExpertsViewModel by lazy {
        ViewModelProvider(this).get(
            TalkToExpertsViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_talk_to_expert)

        supportFragmentManager.beginTransaction()
            .replace(R.id.expertsFrameContainer, TalktoExpertsHomeFragment())
            .commit()
    }
}