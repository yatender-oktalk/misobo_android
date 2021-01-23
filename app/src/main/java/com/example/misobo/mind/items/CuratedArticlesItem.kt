package com.example.misobo.mind.items

import com.example.misobo.R
import com.example.misobo.blogs.BlogsModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.curated_articles_item_layout.view.*

class CuratedArticlesItem(val blogsModel: BlogsModel, val onCLick: (Int) -> Unit) : Item() {
    val adapter = GroupAdapter<ViewHolder>()

    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.curatedArticlesRecyclerView.adapter = adapter
        adapter.clear()
        val blogSection = Section()
        blogsModel.data?.forEachIndexed { index,model ->
            blogSection.add(ArticlesRecyclerItem(model) {
                onCLick.invoke(index)
            })
        }
        adapter.add(blogSection)
    }

    override fun getLayout(): Int = R.layout.curated_articles_item_layout
}