package com.example.misobo.talkToExperts.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.misobo.R
import com.example.misobo.talkToExperts.models.BookSlotPayload
import com.example.misobo.talkToExperts.viewModels.MobileRegistration
import com.example.misobo.talkToExperts.models.OtpPayload
import com.example.misobo.talkToExperts.viewModels.BookSlotState
import com.example.misobo.talkToExperts.viewModels.TalkToExpertsViewModel
import com.example.misobo.utils.SharedPreferenceManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.enter_otp_dialog.*
import kotlinx.android.synthetic.main.enter_otp_dialog.chatWithExperts
import kotlinx.android.synthetic.main.enter_otp_dialog.otpText
import java.util.concurrent.TimeUnit

class OtpDialog : BottomSheetDialogFragment() {

    val viewModel: TalkToExpertsViewModel by activityViewModels()
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
            val OtpPayload =
                OtpPayload(viewModel.mobileNumber.value.toString())
            viewModel.mobileRegistration(OtpPayload)
        }

        viewModel.mobileRegistration.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is MobileRegistration.Success -> {
                    resendOTP()
                    chatWithExperts.text =
                        "OTP resent to ${viewModel.mobileNumber.value.toString()}"
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
                    otpText.text.toString().toInt()
                )
            //Save id here.
            viewModel.verifyOtp(
                SharedPreferenceManager.getUser()?.data?.userId ?: 0,
                verificationPayload
            )
        }

        viewModel.bookSlotLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { state ->
                when (state) {
                    is BookSlotState.Success -> {
                        this.dismiss()
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.add(BookSuccessDialog(), null)?.commit()
                    }
                    is BookSlotState.Loading -> {

                    }
                    is BookSlotState.Fail -> {

                    }
                }
            })

        viewModel.otpVerification.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is MobileRegistration.Success -> {
                    if (SharedPreferenceManager.getUserProfile()?.data?.karmaPoints?.toInt()!! < viewModel.selectedExpertLiveDate.value?.karmaCoinsNeeded ?: 0)
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.add(CoinsBottomSheet(), null)?.commit()
                    else
                        viewModel.selectedExpertLiveDate.value?.id?.let { it1 ->
                            viewModel.bookSlot(
                                it1,
                                BookSlotPayload(
                                    viewModel.slotSelectedLiveData.value
                                )
                            )
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
                resendText.text = "Click to resend OTP"
            }
            .subscribe {
                resendText.text = "Resend in ${32 - it}s"
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