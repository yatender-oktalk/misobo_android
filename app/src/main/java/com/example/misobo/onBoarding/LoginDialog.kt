package com.example.misobo.onBoarding

import android.app.Dialog
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.misobo.R
import com.example.misobo.onBoarding.models.RegistrationModel
import com.example.misobo.onBoarding.viewModels.OnBoardingViewModel
import com.example.misobo.talkToExperts.viewModels.MobileRegistration
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.login_bottom_sheet.*

class LoginDialog : BottomSheetDialogFragment() {

    private val viewModel: OnBoardingViewModel by activityViewModels()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_bottom_sheet, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        crossButton.setOnClickListener {
            this.dismiss()
        }

        compositeDisposable.add(RxTextView.textChanges(otpText)
            .subscribe {
                if (it.length >= 10) {
                    sendOtpButton.alpha = 1f
                    sendOtpButton.isEnabled = true
                } else {
                    sendOtpButton.alpha = .7f
                    sendOtpButton.isEnabled = false
                }
            })

        viewModel.mobileRegistration.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is MobileRegistration.Success -> {

                }
                is MobileRegistration.Fail -> {
                }
                is MobileRegistration.Loading -> {
                }
            }
        })

        viewModel.userLiveData.observe(viewLifecycleOwner, Observer { user ->
            //this.dismiss()
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(OtpDialog(), null)?.commit()
        })

        sendOtpButton.setOnClickListener {
            if (!otpText.text.isNullOrEmpty() || !otpText.text.isNullOrBlank()) {
                viewModel.mobileNumber.postValue(otpText.text.toString())
                val registrationModel =
                    RegistrationModel(
                        otpText.text.toString(),
                        Settings.Secure.getString(
                            activity?.contentResolver,
                            Settings.Secure.ANDROID_ID
                        )
                    )
                viewModel.registerUser(registrationModel)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please enter your Phone number",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val contentView =
            View.inflate(context, R.layout.login_bottom_sheet, null)
        dialog.setContentView(contentView)
        (contentView.parent as View).setBackgroundColor(
            resources.getColor(android.R.color.transparent)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}