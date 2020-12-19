package com.example.misobo.mind.items

import com.example.misobo.R
import com.example.misobo.blogs.BlogsModel
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.articles_item_detailed_layout.view.*

class DetailedArticlesRecyclerItem(
    val model: BlogsModel.Blogs,
    val onClick: (Int) -> Unit
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.articleTitle.text = model.title
        viewHolder.itemView.articlesDetailsTextView.text = model.content
        viewHolder.itemView.readingTime.text = "${model.timeToRead} read"
        viewHolder.itemView.soul.text = model.category ?: "MIND"
        viewHolder.itemView.setOnClickListener { onClick.invoke(position) }
    }

    override fun getLayout(): Int = R.layout.articles_item_detailed_layout
}