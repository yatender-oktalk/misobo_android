package com.example.misobo.talkToExperts.items

import android.view.ViewGroup
import com.example.misobo.R
import com.example.misobo.talkToExperts.models.ExpertCategoriesModel
import com.example.misobo.talkToExperts.view.ExpertPagerAdapter
import com.example.misobo.talkToExperts.view.TalktoExpertsHomeFragment
import com.example.misobo.talkToExperts.viewModels.CategoriesState
import com.example.misobo.utils.Util
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.experts_recycler_list_item_layout.view.*

class ExpertsViewPagerItem(
    val activity: TalktoExpertsHomeFragment,
    val state: CategoriesState.Success
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {

        val expertList = mutableListOf(
            ExpertCategoriesModel(
                -1,
                "All"))

        viewHolder.itemView.tabLayout.setupWithViewPager(viewHolder.itemView.expertViewPager)
        viewHolder.itemView.tabLayout.tabRippleColor = null

        expertList.addAll(state.categoriesModel)
        viewHolder.itemView.expertViewPager.adapter =
            ExpertPagerAdapter(
                activity.childFragmentManager,
                expertList
            )

        //viewHolder.itemView.expertViewPager.redr

        for (i in 0 until viewHolder.itemView.tabLayout.tabCount) {
            val tab = (viewHolder.itemView.tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            if (i == 0) p.setMargins(
                Util.convertDpToPixels(16F, viewHolder.itemView.context),
                0,
                Util.convertDpToPixels(8F, viewHolder.itemView.context),
                0
            )
            else p.setMargins(0, 0, Util.convertDpToPixels(8F, viewHolder.itemView.context), 0)
            tab.requestLayout()
        }

    }

    fun update(){
        notifyChanged()

    }

    override fun getLayout(): Int = R.layout.experts_recycler_list_item_layout
}