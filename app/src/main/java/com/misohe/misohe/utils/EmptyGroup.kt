package com.misohe.misohe.utils

import com.misohe.misohe.R
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder

open class EmptyGroup : Item(), ExpandableItem {

    override fun bind(viewHolder: ViewHolder, position: Int) {}

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {}

    override fun getLayout() = R.layout.empty

}
