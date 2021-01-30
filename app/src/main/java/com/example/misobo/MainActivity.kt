package com.example.misobo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.misobo.blogs.BlogsViewModel
import com.example.misobo.home.HomeFragment
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
import com.google.firebase.analytics.FirebaseAnalytics
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
                    val firebaseAnalytics = FirebaseAnalytics.getInstance(this)
                    firebaseAnalytics.logEvent("rewards_tap", null);
                    val fragment = RewardsFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.mainContainer, fragment, fragment.javaClass.simpleName)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.profile -> {
                    val firebaseAnalytics = FirebaseAnalytics.getInstance(this)
                    firebaseAnalytics.logEvent("profile_tap", null);
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

    override fun onBackPressed() {

        val seletedItemId = bottomNavigationView.selectedItemId
        if (R.id.home != seletedItemId) {
            bottomNavigationView.selectedItemId = R.id.home
            //setHomeItem(this@MainActivity)
        } else {
            super.onBackPressed()
        }
/*
        MaterialDialog(this).show {
            cornerRadius(16f)
            title(text = "Misohe")
            message(text = "Are you sure you want to close this app?")
            positiveButton(text = "Yes") {
                finish()
            }
            negativeButton(text = "Cancel") {
                dismiss()
            }
                .onShow{ it.getActionButton(WhichButton.NEGATIVE).updateTextColor(Color.GRAY) }
                .onShow { it.getActionButton(WhichButton.POSITIVE).updateTextColor(Color.GRAY) }
        }*/
    }
}