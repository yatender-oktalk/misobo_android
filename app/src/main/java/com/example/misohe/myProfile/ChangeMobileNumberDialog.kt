package com.example.misohe.myProfile

import android.app.Dialog
import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.misohe.R
import com.example.misohe.utils.SharedPreferenceManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.JsonObject
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.change_phone_dialog.crossButton
import kotlinx.android.synthetic.main.change_phone_dialog.otpText
import kotlinx.android.synthetic.main.change_phone_dialog.sendOtpButton

class ChangeMobileNumberDialog : BottomSheetDialogFragment() {

    val compositeDisposable = CompositeDisposable()
    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.change_phone_dialog, container, false)
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val contentView =
            View.inflate(context, R.layout.change_phone_dialog, null)
        dialog.setContentView(contentView)
        (contentView.parent as View).setBackgroundColor(
            ContextCompat.getColor(requireContext(), android.R.color.transparent)
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog =
            super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener { dialog: DialogInterface ->
            val dialogc = dialog as BottomSheetDialog
            // When using AndroidX the resource can be found at com.google.android.material.R.id.design_bottom_sheet
            val bottomSheet =
                dialogc.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            val bottomSheetBehavior: BottomSheetBehavior<*> =
                BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!)
            bottomSheetBehavior.peekHeight = Resources.getSystem().displayMetrics.heightPixels
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        }
        return bottomSheetDialog
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        crossButton.setOnClickListener {
            dismiss()
        }

        sendOtpButton.setOnClickListener {
            if (!otpText.text.isNullOrEmpty() || !otpText.text.isNullOrBlank()) {
                profileViewModel.mobileNumber.postValue(otpText.text.toString())
                val jsonObject = JsonObject()
                jsonObject.addProperty("phone", otpText.text.toString())
                profileViewModel.updatePhone(
                    SharedPreferenceManager.getUserProfile()?.data?.id ?: 0, jsonObject
                )
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please enter your Phone number",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        profileViewModel.updatePhoneLiveData.observe(viewLifecycleOwner, Observer {
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(ChangePhoneOtpDialog(), null)?.commit()
        })

        compositeDisposable.add(
            RxTextView.textChanges(otpText)
                .subscribe {
                    if (it.isNotEmpty()) {
                        sendOtpButton.alpha = 1f
                        sendOtpButton.isEnabled = true
                    } else {
                        sendOtpButton.alpha = .7f
                        sendOtpButton.isEnabled = false
                    }
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}