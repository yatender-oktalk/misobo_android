package com.example.misohe

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.callbacks.onShow
import com.example.misohe.blogs.BlogsDetailFragment
import com.example.misohe.blogs.BlogsViewModel
import com.example.misohe.home.HomeFragment
import com.example.misohe.mind.view.ArticlesFragment
import com.example.misohe.mind.view.MindFragment
import com.example.misohe.mind.viewModels.MindViewModel
import com.example.misohe.myProfile.MyProfileFragment
import com.example.misohe.myProfile.ProfileViewModel
import com.example.misohe.rewards.ClaimedRewardsFragment
import com.example.misohe.rewards.RewardsFragment
import com.example.misohe.rewards.RewardsViewModel
import com.example.misohe.talkToExperts.models.CaptureOrderPayload
import com.example.misohe.talkToExperts.viewModels.TalkToExpertsViewModel
import com.example.misohe.utils.SharedPreferenceManager
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
        bottomNavGroup.visibility = View.VISIBLE
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
        val frag = supportFragmentManager.findFragmentById(R.id.mainContainer)
        if (R.id.home != seletedItemId && frag !is ClaimedRewardsFragment && frag !is ArticlesFragment && frag !is BlogsDetailFragment) {
            bottomNavigationView.selectedItemId = R.id.home
            bottomNavGroup.visibility = View.VISIBLE
            //setHomeItem(this@MainActivity)
        } else if (R.id.home == seletedItemId && bottomNavigationView.isVisible) {
            MaterialDialog(this).show {
                cornerRadius(16f)
                title(text = "misohe")
                message(text = "Are you sure you want to close this app?")
                positiveButton(text = "Yes") {
                    super.onBackPressed()
                }
                negativeButton(text = "Cancel") {
                    dismiss()
                }
                    .onShow { it.getActionButton(WhichButton.NEGATIVE).updateTextColor(Color.GRAY) }
                    .onShow { it.getActionButton(WhichButton.POSITIVE).updateTextColor(Color.GRAY) }
            }
        } else {
            super.onBackPressed()
        }
    }
}