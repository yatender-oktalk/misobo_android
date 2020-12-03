package com.example.misobo.mind.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.misobo.R
import com.example.misobo.blogs.BlogsViewModel
import com.example.misobo.mind.viewModels.MindViewModel

class MindActivity : AppCompatActivity() {

    private val viewModel: MindViewModel by lazy { ViewModelProvider(this).get(MindViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mind)

        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.mindFrameContainer,
                MindFragment()
            )
            .commit()
    }
}