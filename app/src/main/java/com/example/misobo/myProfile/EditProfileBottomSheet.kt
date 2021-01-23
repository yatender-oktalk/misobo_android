package com.example.misobo.myProfile

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.misobo.R
import com.example.misobo.onBoarding.OtpDialog
import com.example.misobo.utils.SharedPreferenceManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.JsonObject
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.edit_profile_dialog.*
import kotlinx.android.synthetic.main.edit_profile_dialog.profileImage

class EditProfileBottomSheet : BottomSheetDialogFragment() {

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
        return inflater.inflate(R.layout.edit_profile_dialog, container, false)
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val contentView =
            View.inflate(context, R.layout.edit_profile_dialog, null)
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

        Glide.with(requireContext())
            .load(SharedPreferenceManager.getProfileImage())
            .placeholder(R.drawable.profile_placeholder)
            .error(R.drawable.profile_placeholder)
            .into(profileImage)

        mobileEditText.setText(SharedPreferenceManager.getUserProfile()?.data?.phone.toString())
        nameEditText.setText(SharedPreferenceManager.getUserProfile()?.data?.name ?: "")
        emailEditText.setText(SharedPreferenceManager.getUserProfile()?.data?.email ?: "")
        crossButton.setOnClickListener { this.dismiss() }

        profileImage.setOnClickListener {
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(requireContext(), this)
        }

        editPencil.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(ChangeMobileNumberDialog(), null)?.commit()
        }

        mobileEditText.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(ChangeMobileNumberDialog(), null)?.commit()
        }

        saveChangesButton.setOnClickListener {
            if (!nameEditText.text.isNullOrEmpty() && emailEditText.text.isNullOrEmpty()) {
                val jsonObject = JsonObject()
                jsonObject.addProperty("name", nameEditText.text.toString())
                jsonObject.addProperty("email", emailEditText.text.toString())
                profileViewModel.updateName(
                    SharedPreferenceManager.getUserProfile()?.data?.id ?: 0,
                    jsonObject
                )
            }
        }

        profileViewModel.updateProfile.observe(viewLifecycleOwner, Observer {
            activity?.supportFragmentManager?.fragments
                .takeIf { it?.isNotEmpty() ?: false }
                ?.map { (it as? BottomSheetDialogFragment)?.dismiss() }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri: Uri = result.uri
                SharedPreferenceManager.setProfileImage(resultUri.toString())
                Glide.with(requireContext())
                    .load(resultUri)
                    .placeholder(R.drawable.profile_placeholder)
                    .error(R.drawable.profile_placeholder)
                    .into(profileImage);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //compositeDisposable.dispose()
    }
}