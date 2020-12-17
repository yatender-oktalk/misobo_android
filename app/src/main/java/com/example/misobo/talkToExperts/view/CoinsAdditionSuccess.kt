package com.example.misobo.talkToExperts.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.misobo.R
import com.example.misobo.myProfile.ProfileViewModel
import com.example.misobo.onBoarding.LoginDialog
import com.example.misobo.talkToExperts.models.BookSlotPayload
import com.example.misobo.talkToExperts.viewModels.BookSlotState
import com.example.misobo.talkToExperts.viewModels.TalkToExpertsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.coins_addition_success.*

class CoinsAdditionSuccess : BottomSheetDialogFragment() {

    val viewModel: TalkToExpertsViewModel by activityViewModels()
    val profileViewModel: ProfileViewModel by lazy { ViewModelProvider(this).get(ProfileViewModel::class.java) }
    val compositeDisposable = CompositeDisposable()
    private val groupAdapter = GroupAdapter<ViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.coins_addition_success, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        karmaCoinsText.text =viewModel.pack.toString()

        viewModel.selectedExpertLiveDate.value?.id?.let { it1 ->
            viewModel.bookSlot(
                it1,
                BookSlotPayload(
                    viewModel.slotSelectedLiveData.value
                )
            )
        }

        viewModel.bookSlotLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { state ->
                when (state) {
                    is BookSlotState.Success -> {
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.add(BookSuccessDialog(), null)?.commit()
                    }
                    is BookSlotState.NotAuthorised -> {
                        this.dismiss()
                        val loginDialog =
                            LoginDialog()
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.add(loginDialog, null)?.commit()
                    }
                    is BookSlotState.NotSufficientKarma->{
                        val loginDialog =
                            LoginDialog()
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.add(CoinsBottomSheet(), null)?.commit()
                    }
                    is BookSlotState.Loading -> {

                    }
                    is BookSlotState.Fail -> {

                    }
                }
            })

    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val contentView =
            View.inflate(context, R.layout.coins_addition_success, null)
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