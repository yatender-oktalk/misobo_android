package com.example.misobo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.misobo.home.HomeFragment
import com.example.misobo.myProfile.MyProfileFragment
import com.example.misobo.rewards.RewardsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val fragment = HomeFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, fragment, fragment.javaClass.simpleName)
                .commit()
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    val fragment = HomeFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.mainContainer, fragment, fragment.javaClass.simpleName)
                        .commit()
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