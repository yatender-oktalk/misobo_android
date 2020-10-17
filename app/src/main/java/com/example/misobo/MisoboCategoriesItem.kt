package com.example.misobo

import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.categories_recycler_item.view.*

class MisoboCategoriesItem(private val categoryText: String) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.categoriesNameTextView.text = categoryText
    }

    override fun getLayout(): Int = R.layout.categories_recycler_item
}