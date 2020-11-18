package com.example.misobo.talkToExperts.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.misobo.talkToExperts.models.ExpertCategoriesModel

class ExpertPagerAdapter(fm: FragmentManager, val categoriesList: List<ExpertCategoriesModel>) :
    FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        if (categoriesList.isNotEmpty()) {
            return ExpertsFragment.newInstance(
                categoriesList.get(position)
            )
        } else
            return ExpertsFragment.newInstance(
                categoriesList.get(position)
            )
    }

    override fun getCount(): Int {
        if (categoriesList.isNotEmpty())
            return categoriesList.size
        else
            return 1
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (categoriesList.isNotEmpty())
            categoriesList.get(position).name
        else ""

    }
}