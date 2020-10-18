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
        val categoryList =
            listOf("Reduce Stress", "Worry less", "Feel Empowered", "Sound Sleep", "Horoscope")

        categoryList.forEach { category ->

        }

        categoriesBackIcon.setOnClickListener {
            activity?.onBackPressed()
        }

        onBoardingViewModel.getOnBoardingCategories()
        onBoardingViewModel.categoriesLiveData.observe(viewLifecycleOwner, Observer { it ->
            when (it) {
                is CategoriesAction.Success -> {
                    groupAdapter.clear()
                    val section  = Section()
                    groupAdapter.add(section)
                    it.categoryModel.data?.forEach {
                        section.add(MisoboCategoriesItem(it.name.toString()))
                    }
                }
                is CategoriesAction.Failure -> {
                    Log.i("fail", it.localizedMessage.toString())

                }
                is CategoriesAction.Loading -> {

                }
            }
        })
    }
}