package com.example.misobo.onBoarding.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.misobo.*
import com.example.misobo.onBoarding.models.CategoriesModel
import com.example.misobo.onBoarding.models.CategoriesRequestModel
import com.example.misobo.onBoarding.viewModels.CategoriesAction
import com.example.misobo.onBoarding.viewModels.OnBoardingViewModel
import com.example.misobo.onBoarding.viewModels.ResponseAction
import com.example.misobo.utils.SharedPreferenceManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_misobo_categories.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MisoboCategoriesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MisoboCategoriesFragment : Fragment() {

    val groupAdapter = GroupAdapter<ViewHolder>()
    val onBoardingViewModel: OnBoardingViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_misobo_categories, container, false)
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MisoboCategoriesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        categoriesRecyclerView.adapter = groupAdapter
        categoriesBackIcon.setOnClickListener {
            onBoardingViewModel.categorySelectedPosition.postValue(-1)
            activity?.onBackPressed()
        }

        categoriesContinueButton.setOnClickListener {
            if (onBoardingViewModel.categorySelectedPosition.value != -1) {
                onBoardingViewModel.saveCategories(
                    CategoriesRequestModel(
                        listOf(
                            onBoardingViewModel.categorySelectedPosition.value
                        )
                    ),
                    SharedPreferenceManager.getUser()?.data?.id ?: -1
                )
            } else {
                Toast.makeText(context,"Please select a category" ,Toast.LENGTH_SHORT).show()
            }
        }

        onBoardingViewModel.categoryResponseAction.observe(viewLifecycleOwner, Observer { it ->
            when (it) {
                is ResponseAction.Success -> {
                    categoriesContinueButton.isEnabled = true
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(
                            R.id.onBoardingFrameContainer,
                            SubCategoriesFragment()
                        )
                        ?.addToBackStack(null)
                        ?.commitAllowingStateLoss()
                }
                is ResponseAction.Loading -> categoriesContinueButton.isEnabled = false
                is ResponseAction.Failure -> {
                    categoriesContinueButton.isEnabled = true
                    Toast.makeText(context,"Please try again" ,Toast.LENGTH_SHORT).show()
                }
            }
        })

        onBoardingViewModel.getOnBoardingCategories()
        onBoardingViewModel.categoriesLiveData.observe(viewLifecycleOwner, Observer { it ->
            when (it) {
                is CategoriesAction.Success -> {
                    inflateRecyclerView(
                        it.categoryModel.data,
                        onBoardingViewModel.categorySelectedPosition.value ?: -1
                    )
                }
                is CategoriesAction.Failure -> {
                    Log.i("fail", it.localizedMessage.toString())
                }
                is CategoriesAction.Loading -> {
                }
            }
        })

        onBoardingViewModel.categorySelectedPosition.observe(
            viewLifecycleOwner,
            Observer { position ->
                if (position != -1) {
                    categoriesContinueButton.isEnabled = true
                    categoriesContinueButton.alpha = 1F
                } else {
                    categoriesContinueButton.isEnabled = false
                    categoriesContinueButton.alpha = 0.7F
                }
            })
    }

    private fun inflateRecyclerView(
        it: List<CategoriesModel.Category>?,
        selectedPosition: Int = -1
    ) {
        groupAdapter.clear()
        val section = Section()
        groupAdapter.add(section)
        it?.forEach { category ->
            section.add(
                MisoboCategoriesItem(
                    category.name.toString(),
                    selectedPosition
                ) { position ->
                    onBoardingViewModel.categorySelectedPosition.postValue(position)
                    inflateRecyclerView(it, position)
                })
        }
    }
}