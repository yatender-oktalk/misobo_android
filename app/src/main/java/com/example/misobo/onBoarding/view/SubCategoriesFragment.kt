package com.example.misobo.onBoarding.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.misobo.R
import com.example.misobo.utils.SharedPreferenceManager
import com.example.misobo.onBoarding.models.CategoriesModel
import com.example.misobo.onBoarding.models.CategoriesRequestModel
import com.example.misobo.onBoarding.viewModels.OnBoardingViewModel
import com.example.misobo.onBoarding.viewModels.ResponseAction
import com.google.firebase.analytics.FirebaseAnalytics
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_sub_categories.*

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
        categoryNameTextView.text = when (onBoardingViewModel.categories.value?.data?.get(
            onBoardingViewModel.categorySelectedPosition.value ?: -1
        )?.name) {
            "Reduce Stress" -> "What stresses you the most?"
            "Reduce Anxiety " -> "What makes you anxious?"
            "Negativity " -> "What brings in negativity?"
            "Depression " -> "Which area of life brings sadness for you?"
            "Sound Sleep" -> "What bothers your sleep the most?"
            "Feel Empowered " -> "Feel Empowered"
            "Parent Concerns" -> "I am a Parent"
            "Teen Concerns" -> "I am a teen"
            else -> ""
        }

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
            if (onBoardingViewModel.subCategorySelectedPosition.value != -1) {
                val firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())
                val bundle = Bundle()
                val category = onBoardingViewModel.categories.value?.data?.get(
                    onBoardingViewModel.categorySelectedPosition.value ?: -1
                )?.name

                val selectedSubCategory =
                    subCategory?.get(onBoardingViewModel.subCategorySelectedPosition.value ?: 0)
                bundle.putString(
                    category.toString().replace("\\s+", "_").toLowerCase(),
                    selectedSubCategory.toString().replace("\\s+", "_").toLowerCase()
                )

                firebaseAnalytics.logEvent("user_interest", bundle);
                onBoardingViewModel.saveSubCategories(
                    CategoriesRequestModel(
                        subCategories = listOf(
                            onBoardingViewModel.subCategorySelectedPosition.value?.plus(
                                1
                            )
                        )
                    ),
                    SharedPreferenceManager.getUser()?.data?.registrationId ?: -1
                )
            } else {
                Toast.makeText(context, "Please select a category", Toast.LENGTH_SHORT).show()
            }
        }

        onBoardingViewModel.subCategoryResponseAction.observe(viewLifecycleOwner, Observer { it ->
            when (it) {
                is ResponseAction.Success -> {
                    subCategoriesContinueButton.isEnabled = true
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(
                            R.id.onBoardingFrameContainer,
                            ReminderFragment()
                        )
                        ?.addToBackStack(null)
                        ?.commitAllowingStateLoss()
                }
                is ResponseAction.Loading -> subCategoriesContinueButton.isEnabled = false
                is ResponseAction.Failure -> {
                    subCategoriesContinueButton.isEnabled = true
                    Toast.makeText(context, "Please try again", Toast.LENGTH_SHORT).show()
                }
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