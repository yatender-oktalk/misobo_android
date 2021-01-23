package com.example.misobo.talkToExperts

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.misobo.R
import com.example.misobo.utils.SharedPreferenceManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jakewharton.rxbinding2.widget.RxTextView
import kotlinx.android.synthetic.main.login_bottom_sheet.*

class LoginDialog : BottomSheetDialogFragment() {

    val viewModel: TalkToExpertsViewModel by activityViewModels()

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

        RxTextView.textChanges(otpText)
            .subscribe {
                if (it.length >= 10) {
                    sendOtpButton.alpha = 1f
                    sendOtpButton.isEnabled = true
                } else {
                    sendOtpButton.alpha = .7f
                    sendOtpButton.isEnabled = false
                }
            }

        viewModel.mobileRegistration.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is MobileRegistration.Success -> {
                    viewModel.mobileNumber.postValue(state.verificationResponse.data.phone)
                    val otpDialog = OtpDialog()
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.add(otpDialog, null)?.commit()
                }
                is MobileRegistration.Fail -> {

                }
                is MobileRegistration.Loading -> {

                }
            }
        })

        sendOtpButton.setOnClickListener {
            val OtpPayload = OtpPayload(otpText.text.toString())
            viewModel.mobileRegistration(OtpPayload)
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
}