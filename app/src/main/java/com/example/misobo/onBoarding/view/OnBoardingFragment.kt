package com.example.misobo.onBoarding.view

import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.misobo.BuildConfig
import com.example.misobo.R
import com.example.misobo.onBoarding.LoginDialog
import com.example.misobo.onBoarding.models.RegistrationModel
import com.example.misobo.onBoarding.viewModels.OnBoardingViewModel
import com.example.misobo.utils.SharedPreferenceManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_on_boarding.*
import kotlinx.android.synthetic.main.login_bottom_sheet.*
import java.util.concurrent.TimeUnit

class OnBoardingFragment : Fragment() {

    val onBoardingViewModel: OnBoardingViewModel by activityViewModels()
    private val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_on_boarding, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val textList = listOf("mind", "body", "soul")
        val aniFade = AnimationUtils.loadAnimation(context, R.anim.mind_body_soul_anim)

        privacyTextView.setOnClickListener {
            val url = "${BuildConfig.MISOBO_BASE_URL}/api/terms"
            val builder = CustomTabsIntent.Builder();
            val customTabsIntent = builder.build();
            customTabsIntent.launchUrl(requireContext(), Uri.parse(url));
        }

        disposable.add(Observable.interval(0, 2, TimeUnit.SECONDS)
            .map { textList[it.toInt()] }
            .retry()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mindTextView.startAnimation(aniFade)
                mindTextView.text = it
            }, {})
        )

        try {

        } catch (e: Exception) {
            Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }

        registerText.setOnClickListener {

            val loginDialog =
                LoginDialog()
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(loginDialog, null)?.commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}