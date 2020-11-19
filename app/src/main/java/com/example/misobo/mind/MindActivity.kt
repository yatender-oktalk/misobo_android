package com.example.misobo.mind

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.misobo.R

class MindActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mind)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mindFrameContainer, MindFragment())
            .commit()
    }
}