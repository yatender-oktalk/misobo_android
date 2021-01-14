package com.example.misobo.talkToExperts.view

import android.app.Dialog
import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.misobo.R
import com.example.misobo.myProfile.ProfileViewModel
import com.example.misobo.talkToExperts.viewModels.TalkToExpertsViewModel
import com.example.misobo.utils.SharedPreferenceManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.book_success_dialog.*

class BookSuccessDialog : BottomSheetDialogFragment() {

    val viewModel: TalkToExpertsViewModel by activityViewModels()
    val profileViewModel: ProfileViewModel by lazy { ViewModelProvider(this).get(ProfileViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.book_success_dialog, container, false)
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val contentView =
            View.inflate(context, R.layout.book_success_dialog, null)
        dialog.setContentView(contentView)
        (contentView.parent as View).setBackgroundColor(
            resources.getColor(android.R.color.transparent)
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
            bottomSheetDialog.setCancelable(false)
            bottomSheetBehavior.peekHeight = Resources.getSystem().displayMetrics.heightPixels
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        }
        return bottomSheetDialog
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //if (viewModel.userBookingsLiveData.value?.dataSource != null)

        //viewModel.getUserBookings(SharedPreferenceManager.getUser()?.data?.userId.toString())

        profileViewModel.getProfile(
            SharedPreferenceManager.getUser()?.data?.userId ?: 0
        )

        viewModel.selectedExpertLiveDate.observe(viewLifecycleOwner, Observer { response ->
            nameTextView.text = response.name
            expertLanguage.text = response.language
            Glide.with(requireContext()).load(response.image).placeholder(R.color.colorAccent)
                .into(dailyCheckinStatusImage)
            expertCategory.text = response.qualification?:""
        })

        okayButton.setOnClickListener {
            activity?.supportFragmentManager?.fragments
                .takeIf { it?.isNotEmpty() ?: false }
                ?.map { (it as? BottomSheetDialogFragment)?.dismiss() }
            //this.dismiss()
        }
    }
}