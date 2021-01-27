package com.example.misobo.talkToExperts.view

import android.app.Dialog
import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.misobo.Misobo
import com.example.misobo.R
import com.example.misobo.onBoarding.LoginDialog
import com.example.misobo.talkToExperts.items.DateRecyclerItem
import com.example.misobo.talkToExperts.items.SlotsRecyclerItem
import com.example.misobo.talkToExperts.models.BookSlotPayload
import com.example.misobo.talkToExperts.models.DatePayloadModel
import com.example.misobo.talkToExperts.models.ExpertSlotsResponse
import com.example.misobo.talkToExperts.viewModels.BookSlotState
import com.example.misobo.talkToExperts.viewModels.SlotFetchState
import com.example.misobo.talkToExperts.viewModels.TalkToExpertsViewModel
import com.example.misobo.utils.AuthState
import com.example.misobo.utils.SharedPreferenceManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.slot_booking_bottom_sheet.*
import java.text.SimpleDateFormat
import java.util.*

class BookASlotDialog : BottomSheetDialogFragment() {

    private val dateAdapter = GroupAdapter<ViewHolder>()
    private val slotAdapter = GroupAdapter<ViewHolder>()
    private val viewModel: TalkToExpertsViewModel by activityViewModels()
    private val id by lazy { arguments?.getInt("ID") }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.slot_booking_bottom_sheet, container, false)
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val contentView =
            View.inflate(context, R.layout.slot_booking_bottom_sheet, null)
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

        dateRecyclerView.adapter = dateAdapter
        crossIcon.setOnClickListener { this.dismiss() }

        val layoutManager = FlexboxLayoutManager(context)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = (JustifyContent.FLEX_START)
        timeSlotsRecyclerView.adapter = slotAdapter
        timeSlotsRecyclerView.layoutManager = layoutManager

        id?.let {
            viewModel.getSlot(
                it,
                DatePayloadModel(
                    getDate(
                        0
                    )
                )
            )
        }
        inflateRecycer(0)

        viewModel.selectedExpertLiveDate.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { expert ->
                expertNameTexView.text = expert.name
                expertLanguage.text = expert.language
                expertCategoryTextView.text = expert.qualification ?: ""
                Glide.with(requireContext()).load(expert.image).placeholder(R.color.colorAccent)
                    .into(expertImage)
            })

        viewModel.slotListLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { state ->
                when (state) {
                    is SlotFetchState.Success -> {
                        viewModel.slotSelectedLiveData.postValue(null)
                        inflateSlotsRecycler(state.slotList, -1)
                        if (state.slotList.isEmpty()) {
                            Toast.makeText(
                                context,
                                "Please select a different date.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    is SlotFetchState.Loading -> {

                    }
                    is SlotFetchState.Fail -> {

                    }
                }
            })

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
                        SharedPreferenceManager.clear()
                        Misobo.authRelay.onNext(AuthState.FAILED)
                    }
                    is BookSlotState.NotSufficientKarma -> {
                        val bundle = Bundle()
                        bundle.putString("TYPE", "CALL")
                        val coinsBottomSheet = CoinsBottomSheet()
                        coinsBottomSheet.arguments = bundle
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.add(coinsBottomSheet, null)?.commit()
                    }
                    is BookSlotState.Loading -> {

                    }
                    is BookSlotState.Fail -> {

                    }
                }
            })

        okayButton.setOnClickListener {
            id?.let { it1 ->
                viewModel.bookSlot(
                    it1,
                    BookSlotPayload(
                        viewModel.slotSelectedLiveData.value
                    )
                )
            }
        }

        viewModel.slotSelectedLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it != null) {
                okayButton.isEnabled = true
                okayButton.alpha = 1f
            } else {
                okayButton.isEnabled = false
                okayButton.alpha = 0.7f
            }
        })
    }

    private fun inflateSlotsRecycler(slotList: List<ExpertSlotsResponse>, position: Int) {
        slotAdapter.clear()
        val section = Section()
        slotList.filter { it.isBooked?.not() ?: false }.forEach { it ->
            section.add(
                SlotsRecyclerItem(
                    it,
                    position
                ) { unixTime, selPosition ->
                    viewModel.slotSelectedLiveData.postValue(unixTime)
                    inflateSlotsRecycler(slotList, selPosition)
                })
        }
        slotAdapter.add(section)
    }

    private fun inflateRecycer(position: Int) {
        dateAdapter.clear()
        val section = Section()
        for (i in 0..30) {
            section.add(
                DateRecyclerItem(
                    getDate(
                        delay = i
                    ), position
                ) { date, position ->
                    id?.let {
                        viewModel.getSlot(
                            it,
                            DatePayloadModel(
                                date
                            )
                        )
                    }
                    inflateRecycer(position)
                })
        }
        dateAdapter.add(section)
    }

    private fun getDate(delay: Int): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, delay)
        val toDate1 = cal.time
        val fromDate = dateFormat.format(toDate1)
        return fromDate
    }
}