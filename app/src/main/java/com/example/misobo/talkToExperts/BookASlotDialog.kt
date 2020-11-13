package com.example.misobo.talkToExperts

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.misobo.R
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dateRecyclerView.adapter = dateAdapter

        val layoutManager = FlexboxLayoutManager(context)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = (JustifyContent.FLEX_START)
        timeSlotsRecyclerView.adapter = slotAdapter
        timeSlotsRecyclerView.layoutManager = layoutManager

        id?.let {
            viewModel.getSlot(
                it,
                DatePayloadModel(getDate(0))
            )
        }
        inflateRecycer(0)


        viewModel.selectedExpertLiveDate.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { expert ->
                expertNameTexView.text = expert.name
                expertLanguage.text = expert.language
            })

        viewModel.slotListLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { state ->
                when (state) {
                    is SlotFetchState.Success -> {
                        inflateSlotsRecycler(state.slotList)
                    }
                    is SlotFetchState.Loading -> {

                    }
                    is SlotFetchState.Fail -> {

                    }
                }
            })
    }

    private fun inflateSlotsRecycler(slotList: List<ExpertSlotsResponse>) {
        slotAdapter.clear()
        val section = Section()
        slotList.filter { it.isBooked?.not() ?: false }.forEach {
            section.add(SlotsRecyclerItem(it))
        }
        slotAdapter.add(section)
    }

    private fun inflateRecycer(position: Int) {
        dateAdapter.clear()
        val section = Section()
        for (i in 0..30) {
            section.add(DateRecyclerItem(getDate(delay = i), position) { date, position ->
                id?.let {
                    viewModel.getSlot(
                        it,
                        DatePayloadModel(date)
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