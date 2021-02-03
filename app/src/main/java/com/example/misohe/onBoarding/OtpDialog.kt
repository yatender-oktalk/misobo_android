package com.example.misohe.onBoarding

import android.app.Activity
import android.app.Dialog
import android.content.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.misohe.MainActivity
import com.example.misohe.Misohe
import com.example.misohe.R
import com.example.misohe.onBoarding.models.ResendOTPModel
import com.example.misohe.onBoarding.view.MisoboMembersActivity
import com.example.misohe.onBoarding.viewModels.OnBoardingViewModel
import com.example.misohe.onBoarding.viewModels.ResendOTPAction
import com.example.misohe.talkToExperts.models.OtpPayload
import com.example.misohe.talkToExperts.viewModels.MobileRegistration
import com.example.misohe.utils.SharedPreferenceManager
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.enter_otp_dialog.*
import java.util.concurrent.TimeUnit
import java.util.regex.Matcher
import java.util.regex.Pattern

class OtpDialog : BottomSheetDialogFragment() {

    val viewModel: OnBoardingViewModel by activityViewModels()
    val compositeDisposable = CompositeDisposable()
    private val CREDENTIAL_PICKER_REQUEST = 1  // Set to an unused request code
    private val SMS_CONSENT_REQUEST = 2  // Set to an unused request code

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

    private fun requestHint() {
        val hintRequest = HintRequest.Builder()
            .setPhoneNumberIdentifierSupported(true)
            .build()
        val credentialsClient = Credentials.getClient(requireContext())
        val intent = credentialsClient.getHintPickerIntent(hintRequest)
        startIntentSenderForResult(
            intent.intentSender,
            CREDENTIAL_PICKER_REQUEST,
            null, 0, 0, 0, null
        )
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CREDENTIAL_PICKER_REQUEST ->
                // Obtain the phone number from the result
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val credential = data.getParcelableExtra<Credential>(Credential.EXTRA_KEY)
                    // credential.getId();  <-- will need to process phone number string
                }
            // ...
            SMS_CONSENT_REQUEST ->
                // Obtain the phone number from the result
                if (resultCode == Activity.RESULT_OK && data != null) {
                    // Get SMS message content
                    val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                    // Extract one-time code from the message and complete verification
                    // `message` contains the entire text of the SMS message, so you will need
                    // to parse the string.
                    val oneTimeCode = message?.let { parseOneTimeCode(it) }
                    otpText.setText(oneTimeCode)// define this function

                    // send one time code to the server
                } else {
                    // Consent denied. User can type OTC manually.
                }
        }
    }

    private val smsVerificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
                val extras = intent.extras
                val smsRetrieverStatus = extras?.get(SmsRetriever.EXTRA_STATUS) as Status

                when (smsRetrieverStatus.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        // Get consent intent
                        val consentIntent =
                            extras.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)
                        try {
                            // Start activity to show consent dialog to user, activity must be started in
                            // 5 minutes, otherwise you'll receive another TIMEOUT intent
                            if (activity != null) {
                                startActivityForResult(consentIntent, SMS_CONSENT_REQUEST)
                            }
                        } catch (e: ActivityNotFoundException) {
                            // Handle the exception ...
                        }
                    }
                    CommonStatusCodes.TIMEOUT -> {
                        // Time out occurred, handle the error.
                    }
                }
            }
        }
    }

    private fun parseOneTimeCode(message: String): String {
        val pattern: Pattern = Pattern.compile("(\\d{4})")

        val matcher: Matcher = pattern.matcher(message)
        var `val` = ""
        if (matcher.find()) {
            `val` = matcher.group(0) // 4 digit number
        }
        return `val`
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        //registerReceiver(smsVerificationReceiver, intentFilter)
        activity?.registerReceiver(smsVerificationReceiver, intentFilter)

        SmsRetriever.getClient(requireContext()).startSmsUserConsent(null)

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
                    Misohe.instance.sendFcmToken()
                    if (state.profileResponseModel.isNewUser != true) {
                        //startActivity(Intent(context, MisoboMembersActivity::class.java))

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
        activity?.unregisterReceiver(smsVerificationReceiver)
    }

}