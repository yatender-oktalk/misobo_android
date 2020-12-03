package com.example.misobo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.misobo.blogs.BlogsViewModel
import com.example.misobo.home.HomeFragment
import com.example.misobo.mind.view.MindActivity
import com.example.misobo.mind.view.MindFragment
import com.example.misobo.mind.viewModels.MindViewModel
import com.example.misobo.myProfile.MyProfileFragment
import com.example.misobo.rewards.RewardsFragment
import com.example.misobo.utils.SharedPreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel: MindViewModel by lazy { ViewModelProvider(this).get(MindViewModel::class.java) }
    private val blogsViewModel: BlogsViewModel by lazy { ViewModelProvider(this).get(BlogsViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            showHome()
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun showHome() {
        if (SharedPreferenceManager.isBodyUnlocked() || SharedPreferenceManager.isMindUnlocked()) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, MindFragment()).commit()
        } else {
            val fragment = HomeFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, fragment, fragment.javaClass.simpleName)
                .commit()
        }
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    showHome()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.rewards -> {
                    val fragment = RewardsFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.mainContainer, fragment, fragment.javaClass.simpleName)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.profile -> {
                    val fragment = MyProfileFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.mainContainer, fragment, fragment.javaClass.simpleName)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
}