package com.example.misobo.talkToExperts.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.misobo.R
import com.example.misobo.myProfile.ProfileViewModel
import com.example.misobo.talkToExperts.viewModels.TalkToExpertsViewModel
import com.example.misobo.utils.SharedPreferenceManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.book_success_dialog.*

class BookSuccessDialog:BottomSheetDialogFragment() {

    val viewModel: TalkToExpertsViewModel by activityViewModels()
    val profileViewModel: ProfileViewModel by lazy { ViewModelProvider(this).get(ProfileViewModel::class.java) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.book_success_dialog,container,false)
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val contentView =
            View.inflate(context, R.layout.book_success_dialog, null)
        dialog.setContentView(contentView)
        (contentView.parent as View).setBackgroundColor(
            resources.getColor(android.R.color.transparent)
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        profileViewModel.getProfile(
            SharedPreferenceManager.getUser()?.data?.userId ?: 0
        )
        viewModel.selectedExpertLiveDate.observe(viewLifecycleOwner, Observer { response->
            nameTextView.text=response.name
            expertLanguage.text = response.language
        })

        okayButton.setOnClickListener { this.dismiss() }
    }
}