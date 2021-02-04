package com.misohe.misohe.talkToExperts.items

import android.os.Bundle
import android.view.ViewGroup
import com.misohe.misohe.R
import com.misohe.misohe.talkToExperts.models.ExpertCategoriesModel
import com.misohe.misohe.talkToExperts.view.ExpertPagerAdapter
import com.misohe.misohe.talkToExperts.view.TalktoExpertsHomeFragment
import com.misohe.misohe.talkToExperts.viewModels.CategoriesState
import com.misohe.misohe.utils.Util
import com.google.android.material.tabs.TabLayout
import com.google.firebase.analytics.FirebaseAnalytics
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
                "All"
            )
        )

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

        viewHolder.itemView.tabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val firebaseAnalytics = FirebaseAnalytics.getInstance(viewHolder.itemView.context)
                val bundle = Bundle();
                bundle.putString(Util.convertToSnakeCase(tab?.text.toString()), "");
                firebaseAnalytics.logEvent("talk_to_expert", bundle);
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    fun update() {
        notifyChanged()
    }

    override fun getLayout(): Int = R.layout.experts_recycler_list_item_layout
}