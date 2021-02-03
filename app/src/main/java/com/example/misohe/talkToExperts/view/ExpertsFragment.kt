package com.example.misohe.talkToExperts.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.misohe.R
import com.example.misohe.talkToExperts.viewModels.ExpertListState
import com.example.misohe.talkToExperts.items.ExpertsRecyclerItem
import com.example.misohe.talkToExperts.viewModels.TalkToExpertsViewModel
import com.example.misohe.talkToExperts.models.ExpertCategoriesModel
import com.example.misohe.talkToExperts.pagination.ExpertsListAdapter
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_experts.*

private const val CATEGORY_MODEL = "CATEGORY_MODEL"

class ExpertsFragment : Fragment() {
    private var categoryModel: ExpertCategoriesModel? = null
    private val viewModel: TalkToExpertsViewModel by lazy {
        ViewModelProvider(this).get(TalkToExpertsViewModel::class.java)
    }
    private val commonViewModel: TalkToExpertsViewModel by activityViewModels()

    private val groupAdapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryModel = it.getParcelable(CATEGORY_MODEL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_experts, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(categoryModel: ExpertCategoriesModel) =
            ExpertsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CATEGORY_MODEL, categoryModel)
                }
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /* if (categoryModel?.id == -1)
             viewModel.getAllExpertsList()
         else
             viewModel.getCategoryExpertsList(categoryModel?.id ?: 0)*/

        categoryModel?.let {
            viewModel.getCategoryExpertsList(it)
        }

        val adapter = ExpertsListAdapter(){
            commonViewModel.selectedExpertLiveDate.postValue(it)
            val slotDialog =
                BookASlotDialog()
            val bundle = Bundle()
            it?.id?.let { it1 -> bundle.putInt("ID", it1) }
            slotDialog.arguments = bundle
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(slotDialog, null)?.commit()
        }

        viewModel.expertsPagedListLiveData.observe(viewLifecycleOwner, Observer { it->
            adapter.submitList(it)
        })

        expertsRecyclerView.adapter = adapter
        viewModel.expertListLiveData.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is ExpertListState.Success -> {
                    groupAdapter.clear()
                    val section = Section()
                    state.expertList.entries?.forEach {
                        section.add(
                            ExpertsRecyclerItem(
                                it
                            ) {
                                commonViewModel.selectedExpertLiveDate.postValue(it)
                                val slotDialog =
                                    BookASlotDialog()
                                val bundle = Bundle()
                                it.id?.let { it1 -> bundle.putInt("ID", it1) }
                                slotDialog.arguments = bundle
                                activity?.supportFragmentManager?.beginTransaction()
                                    ?.add(slotDialog, null)?.commit()
                            })
                    }
                    groupAdapter.add(section)
                }
                is ExpertListState.Loading -> {

                }
                is ExpertListState.Fail -> {

                }
            }
        })
    }
}