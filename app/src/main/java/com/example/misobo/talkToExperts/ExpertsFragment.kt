package com.example.misobo.talkToExperts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.misobo.R

private const val CATEGORY_MODEL = "CATEGORY_MODEL"

class ExpertsFragment : Fragment() {
    private var categoryModel: ExpertCategoriesModel? = null

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
}