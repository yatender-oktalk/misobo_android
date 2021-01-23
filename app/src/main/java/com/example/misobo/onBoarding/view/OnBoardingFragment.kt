package com.example.misobo.onBoarding.view

import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.misobo.R
import com.example.misobo.onBoarding.models.RegistrationModel
import com.example.misobo.onBoarding.viewModels.OnBoardingViewModel
import com.example.misobo.utils.SharedPreferenceManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_on_boarding.*
import java.util.concurrent.TimeUnit

class OnBoardingFragment : Fragment() {

    val onBoardingViewModel: OnBoardingViewModel by activityViewModels()
    val disposable = CompositeDisposable()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_on_boarding, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val textList = listOf("mind" , "body" , "soul")
        val aniFade = AnimationUtils.loadAnimation(context, R.anim.mind_body_soul_anim)

        disposable.add(Observable.interval(0, 2, TimeUnit.SECONDS)
            .map { textList[it.toInt()] }
            .retry()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                mindTextView.startAnimation(aniFade)
                mindTextView.text = it
            },{}))

        onBoardingViewModel.userLiveData.observe(viewLifecycleOwner, Observer { user ->
            SharedPreferenceManager.setUser(user)
        })

        try {
            val registrationModel =
                RegistrationModel(
                    Settings.Secure.getString(
                        context?.contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                )
           /* if (SharedPreferenceManager.getUser() == null) {
                onBoardingViewModel.registerUser(
                    registrationModel
                )
            }*/
        } catch (e: Exception) {
            Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }

        getStartedText.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(
                    R.id.onBoardingFrameContainer,
                    MisoboMembersFragment()
                )
                ?.addToBackStack(null)
                ?.commitAllowingStateLoss()
        }
    }

}