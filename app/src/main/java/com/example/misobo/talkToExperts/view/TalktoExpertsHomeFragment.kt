package com.example.misobo.talkToExperts.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.misobo.R
import com.example.misobo.talkToExperts.viewModels.CategoriesState
import com.example.misobo.talkToExperts.viewModels.TalkToExpertsViewModel
import com.example.misobo.talkToExperts.models.ExpertCategoriesModel
import com.example.misobo.utils.Util
import kotlinx.android.synthetic.main.fragment_talkto_experts_home.*

class TalktoExpertsHomeFragment : Fragment() {

    val viewModel: TalkToExpertsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_talkto_experts_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.getExpertCategories()

        viewModel.categoriesExpertLiveData.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is CategoriesState.Success -> {
                    val expertList = mutableListOf(
                        ExpertCategoriesModel(
                            -1,
                            "All"
                        )
                    )
                    expertList.addAll(state.categoriesModel)
                    expertViewPager.adapter =
                        ExpertPagerAdapter(
                            requireActivity().supportFragmentManager,
                            expertList
                        )
                    tabLayout.setupWithViewPager(expertViewPager)
                    tabLayout.tabRippleColor = null

                    for (i in 0 until tabLayout.tabCount) {
                        val tab = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
                        val p = tab.layoutParams as MarginLayoutParams
                        if (i == 0) p.setMargins(
                            Util.convertDpToPixels(16F, requireContext()),
                            0,
                            Util.convertDpToPixels(8F, requireContext()),
                            0
                        )
                        else p.setMargins(0, 0, Util.convertDpToPixels(8F, requireContext()), 0)
                        tab.requestLayout()
                    }
                }

                is CategoriesState.Fail -> {

                }
                is CategoriesState.Loading -> {

                }
            }
        })


    }
}