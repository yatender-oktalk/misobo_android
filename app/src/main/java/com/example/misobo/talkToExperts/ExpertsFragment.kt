package com.example.misobo.talkToExperts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.misobo.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_experts.*

private const val CATEGORY_MODEL = "CATEGORY_MODEL"

class ExpertsFragment : Fragment() {
    private var categoryModel: ExpertCategoriesModel? = null
    val viewModel: TalkToExpertsViewModel by lazy {
        ViewModelProvider(this).get(
            TalkToExpertsViewModel::class.java
        )
    }
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

        if (categoryModel?.id == -1)
            viewModel.getAllExpertsList()
        else
            viewModel.getCategoryExpertsList(categoryModel?.id ?: 0)

        expertsRecyclerView.adapter = groupAdapter
        viewModel.expertListLiveData.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is ExpertListState.Success -> {
                    groupAdapter.clear()
                    val section = Section()
                    state.expertList.entries?.forEach {
                        section.add(ExpertsRecyclerItem(it))
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