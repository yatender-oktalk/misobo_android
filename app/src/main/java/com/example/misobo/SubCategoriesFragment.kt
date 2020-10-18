package com.example.misobo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_misobo_categories.*
import kotlinx.android.synthetic.main.fragment_sub_categories.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "SUB_CAT"

class SubCategoriesFragment : Fragment() {

    private val onBoardingViewModel: OnBoardingViewModel by activityViewModels()
    val groupAdapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
        Log.i("sub", onBoardingViewModel.getSubCategory().toString())
        val subCategory = onBoardingViewModel.getSubCategory()
        categoryNameTextView.text =
            onBoardingViewModel.categories.value?.data?.get(
                onBoardingViewModel.categorySelectedPosition.value ?: -1
            )?.name ?: ""
        inflateSubCategory(subCategory)

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
    }

    private fun inflateSubCategory(
        subCategory: List<CategoriesModel.SubCategory>?,
        selectedPosition: Int = -1
    ) {
        groupAdapter.clear()
        val section = Section()
        groupAdapter.add(section)
        subCategory?.forEach { subCategoryItem ->
            section.add(MisoboCategoriesItem(subCategoryItem.name.toString(), selectedPosition) {
                inflateSubCategory(subCategory, it)
                onBoardingViewModel.subCategorySelectedPosition.postValue(it)
            })

        }
    }
}