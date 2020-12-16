package com.example.misobo.rewards

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
import com.bumptech.glide.Glide
import com.example.misobo.R
import com.example.misobo.myProfile.FetchState
import com.example.misobo.talkToExperts.view.CoinsBottomSheet
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.rewards_bottom_sheet.*

class RewardsDetailsBottomSheet : BottomSheetDialogFragment() {

    private val rewardsViewModel: RewardsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.rewards_bottom_sheet, container, false)
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val contentView =
            View.inflate(context, R.layout.rewards_bottom_sheet, null)
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
            bottomSheetBehavior.peekHeight = Resources.getSystem().displayMetrics.heightPixels
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        }
        return bottomSheetDialog
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        crossIcon.setOnClickListener { this.dismiss() }

        rewardsViewModel.selectedRewardLiveData.observe(viewLifecycleOwner, Observer { rewards ->
            Glide.with(requireContext()).load(rewards.img).into(imageView)
            offerDetailsText.text = rewards.offerDetails
            howToRedeemDetails.text = rewards.howToRedeem
            termsAndConditions.text = rewards.termsAndConditions
            coinsText.text = rewards.karma.toString()
            getRewardButton.setOnClickListener { rewardsViewModel.redeemRewards(rewards.id ?: -1) }
        })

        rewardsViewModel.redeemRewardsLiveData.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is RedeemFetchState.Success -> {
                    redeemButtonGroup.visibility = View.GONE
                }
                is RedeemFetchState.Error -> {
                }
                is RedeemFetchState.Loading -> {
                }
                is RedeemFetchState.NotSufficientKarma -> {
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.add(CoinsBottomSheet(), null)?.commit()
                }
            }
        })
    }
}