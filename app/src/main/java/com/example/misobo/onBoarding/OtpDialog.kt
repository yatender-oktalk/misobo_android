package com.example.misobo.onBoarding

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.misobo.MainActivity
import com.example.misobo.Misobo
import com.example.misobo.R
import com.example.misobo.myProfile.ProfileService
import com.example.misobo.onBoarding.models.ResendOTPModel
import com.example.misobo.onBoarding.view.MisoboMembersActivity
import com.example.misobo.onBoarding.view.MisoboMembersFragment
import com.example.misobo.onBoarding.viewModels.OnBoardingViewModel
import com.example.misobo.onBoarding.viewModels.ResendOTPAction
import com.example.misobo.talkToExperts.viewModels.MobileRegistration
import com.example.misobo.talkToExperts.models.OtpPayload
import com.example.misobo.utils.SharedPreferenceManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.JsonObject
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.enter_otp_dialog.*
import kotlinx.android.synthetic.main.enter_otp_dialog.chatWithExperts
import kotlinx.android.synthetic.main.enter_otp_dialog.otpText
import java.util.concurrent.TimeUnit

class OtpDialog : BottomSheetDialogFragment() {

    val viewModel: OnBoardingViewModel by activityViewModels()
    val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.enter_otp_dialog, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        resendOTP()

        backIcon.setOnClickListener {
            this.dismiss()
        }

        crossButton.setOnClickListener {
            this.dismiss()
        }

        resendText.setOnClickListener {
            viewModel.resendOTP(
                SharedPreferenceManager.getUser()?.data?.userId.toString(),
                ResendOTPModel(viewModel.mobileNumber.value.toString())
            )
        }

        viewModel.resendOTPAction.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is ResendOTPAction.Success -> {
                    chatWithExperts.text =
                        "OTP resent to ${viewModel.mobileNumber.value.toString()}"
                    chatWithExperts.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.successGreen
                        )
                    )
                    otpText.text.clear()
                    invalidOtpText.visibility = View.INVISIBLE
                    otpText.backgroundTintList =
                        ContextCompat.getColorStateList(requireContext(), R.color.darkGrey)
                    resendOTP()
                }
            }
        })

        viewModel.mobileRegistration.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is MobileRegistration.Success -> {
                    resendOTP()
                }
                is MobileRegistration.Fail -> {
                }
                is MobileRegistration.Loading -> {
                }
            }
        })

        compositeDisposable.add(RxTextView.textChanges(otpText)
            .subscribe {
                if (it.isNotEmpty()) {
                    verifyButton.alpha = 1f
                    verifyButton.isEnabled = true
                } else {
                    verifyButton.alpha = .7f
                    verifyButton.isEnabled = false
                }
            })

        viewModel.mobileNumber.observe(viewLifecycleOwner, Observer { number ->
            chatWithExperts.text = "OTP sent to $number"
        })

        verifyButton.setOnClickListener {
            val verificationPayload =
                OtpPayload(
                    viewModel.mobileNumber.value.toString(),
                    otpText.text.toString().toInt(),
                    SharedPreferenceManager.getUser()?.data?.registrationId?.toInt()
                )
            //Save id here.
            viewModel.verifyOtp(
                SharedPreferenceManager.getUser()?.data?.userId ?: 0,
                verificationPayload
            )
        }

        viewModel.otpVerification.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is MobileRegistration.Success -> {
                    SharedPreferenceManager.setUserProfile(state.profileResponseModel)
                    Misobo.instance.sendFcmToken()
                    if (state.profileResponseModel.isNewUser != true) {
                        startActivity(Intent(context, MainActivity::class.java))
                        activity?.finish()
                    } else {
                        startActivity(Intent(context, MisoboMembersActivity::class.java))
                        activity?.finish()
                    }
                }
                is MobileRegistration.Loading -> {
                    invalidOtpText.visibility = View.INVISIBLE
                    otpText.backgroundTintList =
                        ContextCompat.getColorStateList(requireContext(), R.color.darkGrey)
                }
                is MobileRegistration.Fail -> {
                    invalidOtpText.visibility = View.VISIBLE
                    otpText.backgroundTintList =
                        ContextCompat.getColorStateList(requireContext(), R.color.invalidOTPRed)
                }
            }
        })
    }

    private fun resendOTP() {
        compositeDisposable.add(Observable.interval(1, TimeUnit.SECONDS)
            .take(32)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                resendText.isClickable = true
                resendText.text = " Resend OTP"
            }
            .subscribe {
                resendText.text = " Resend in ${32 - it}s"
                resendText.isClickable = false
            })
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val contentView =
            View.inflate(context, R.layout.enter_otp_dialog, null)
        dialog.setContentView(contentView)
        (contentView.parent as View).setBackgroundColor(
            ContextCompat.getColor(requireContext(), android.R.color.transparent)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

}