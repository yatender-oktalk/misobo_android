package com.example.misobo.talkToExperts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.misobo.R
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
                    expertViewPager.adapter =
                        ExpertPagerAdapter(
                            requireActivity().supportFragmentManager,
                            state.categoriesModel
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

                    /*TabLayoutMediator(tabLayout, expertViewPager) { tab, position ->
                        tab.text = expertList[position]
                        expertViewPager.setCurrentItem(tab.position, true)
                    }.attach()*/
                }

                is CategoriesState.Fail -> {

                }
                is CategoriesState.Loading -> {

                }
            }
        })


    }
}