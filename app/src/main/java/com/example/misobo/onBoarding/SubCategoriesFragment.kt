package com.example.misobo.onBoarding

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.misobo.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_sub_categories.*

// TODO: Rename parameter arguments, choose names that match

class SubCategoriesFragment : Fragment() {

    private val onBoardingViewModel: OnBoardingViewModel by activityViewModels()
    private val groupAdapter = GroupAdapter<ViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sub_categories, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        subCategoriesRecyclerView.adapter = groupAdapter
        val subCategory = onBoardingViewModel.getSubCategory()
        categoryNameTextView.text =
            onBoardingViewModel.categories.value?.data?.get(
                onBoardingViewModel.categorySelectedPosition.value ?: -1
            )?.name ?: ""
        inflateSubCategory(subCategory, onBoardingViewModel.subCategorySelectedPosition.value ?: -1)

        subCategoriesBackIcon.setOnClickListener {
            onBoardingViewModel.subCategorySelectedPosition.postValue(-1)
            activity?.onBackPressed()
        }

        onBoardingViewModel.subCategorySelectedPosition.observe(
            viewLifecycleOwner,
            Observer { position ->
                if (position != -1) {
                    subCategoriesContinueButton.isEnabled = true
                    subCategoriesContinueButton.alpha = 1F
                } else {
                    subCategoriesContinueButton.isEnabled = false
                    subCategoriesContinueButton.alpha = 0.7F
                }
            })

        subCategoriesContinueButton.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.onBoardingFrameContainer, ReminderFragment())
                ?.addToBackStack(null)
                ?.commitAllowingStateLoss()
        }
    }

    private fun inflateSubCategory(
        subCategory: List<CategoriesModel.SubCategory>?,
        selectedPosition: Int = -1
    ) {
        groupAdapter.clear()
        val section = Section()
        groupAdapter.add(section)
        subCategory?.forEach { subCategoryItem ->
            section.add(
                MisoboCategoriesItem(
                    subCategoryItem.name.toString(),
                    selectedPosition
                ) {
                    inflateSubCategory(subCategory, it)
                    onBoardingViewModel.subCategorySelectedPosition.postValue(it)
                })
        }
    }
}