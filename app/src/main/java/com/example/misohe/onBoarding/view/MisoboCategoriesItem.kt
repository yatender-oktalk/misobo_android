package com.example.misohe.onBoarding.view

import android.view.View
import androidx.core.content.ContextCompat
import com.example.misohe.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.categories_recycler_item.view.*

class MisoboCategoriesItem(
    private val categoryText: String,
    val isSelected: Int,
    val onClick: (Int) -> Unit
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.categoriesNameTextView.text = categoryText
        if (isSelected == position) {
            viewHolder.itemView.tickIcon.visibility = View.VISIBLE
            viewHolder.itemView.root.background = ContextCompat.getDrawable(
                viewHolder.containerView.context,
                R.drawable.rounded_background_stroke
            )
        } else {
            viewHolder.itemView.tickIcon.visibility = View.GONE
            viewHolder.itemView.root.background = ContextCompat.getDrawable(
                viewHolder.containerView.context,
                R.drawable.rounded_background_radius_10
            )
        }
        viewHolder.itemView.root.setOnClickListener { onClick.invoke(position) }
    }

    override fun getLayout(): Int =
        R.layout.categories_recycler_item
}