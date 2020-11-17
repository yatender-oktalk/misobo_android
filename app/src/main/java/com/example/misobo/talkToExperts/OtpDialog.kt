package com.example.misobo.talkToExperts

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.misobo.R
import com.example.misobo.utils.SharedPreferenceManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jakewharton.rxbinding2.widget.RxTextView
import kotlinx.android.synthetic.main.enter_otp_dialog.*
import kotlinx.android.synthetic.main.enter_otp_dialog.otpText

class OtpDialog : BottomSheetDialogFragment() {

    val viewModel: TalkToExpertsViewModel by activityViewModels()

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

        RxTextView.textChanges(otpText)
            .subscribe {
                if (it.isNotEmpty()) {
                    verifyButton.alpha = 1f
                    verifyButton.isEnabled = true
                } else {
                    verifyButton.alpha = .7f
                    verifyButton.isEnabled = false
                }
            }
        verifyButton.setOnClickListener {
            val verificationPayload =
                OtpPayload(viewModel.mobileNumber.value.toString(), otpText.text.toString().toInt())
            viewModel.verifyOtp(
                SharedPreferenceManager.getUser()?.data?.registrationId ?: -1,
                verificationPayload
            )
        }
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val contentView =
            View.inflate(context, R.layout.enter_otp_dialog, null)
        dialog.setContentView(contentView)
        (contentView.parent as View).setBackgroundColor(
            resources.getColor(android.R.color.transparent)
        )
    }

}