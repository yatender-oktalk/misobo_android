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
import com.example.misobo.myProfile.ProfileViewModel
import com.example.misobo.rewards.RewardsFragment
import com.example.misobo.rewards.RewardsViewModel
import com.example.misobo.talkToExperts.models.CaptureOrderPayload
import com.example.misobo.talkToExperts.viewModels.TalkToExpertsViewModel
import com.example.misobo.utils.SharedPreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.razorpay.PaymentResultListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), PaymentResultListener {

    private val viewModel: MindViewModel by lazy { ViewModelProvider(this).get(MindViewModel::class.java) }
    private val blogsViewModel: BlogsViewModel by lazy { ViewModelProvider(this).get(BlogsViewModel::class.java) }
    private val rewardsViewModel: RewardsViewModel by lazy {
        ViewModelProvider(this).get(RewardsViewModel::class.java)
    }
    private val profileViewModel by lazy { ViewModelProvider(this).get(ProfileViewModel::class.java) }

    private val talkToExpertsViewModel: TalkToExpertsViewModel by lazy {
        ViewModelProvider(this).get(
            TalkToExpertsViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            showHome()
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun showHome() {
        if (SharedPreferenceManager.getUserProfile()?.data?.isBodyPackUnlocked == true || SharedPreferenceManager.getUserProfile()?.data?.isMindPackUnlocked == true) {
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

    override fun onPaymentError(p0: Int, p1: String?) {

    }

    fun redirectToRewardstab() {
        bottomNavigationView.selectedItemId = R.id.rewards
    }

    override fun onPaymentSuccess(p0: String?) {
        talkToExpertsViewModel.captureOrder(
            CaptureOrderPayload(
                p0.toString(),
                "signature",
                talkToExpertsViewModel.currentOrder.value?.data?.orderId ?: 0,
                talkToExpertsViewModel.currentOrder.value?.data?.transactionId ?: 0,
                talkToExpertsViewModel.paymentAmount
            )
        )
    }
}