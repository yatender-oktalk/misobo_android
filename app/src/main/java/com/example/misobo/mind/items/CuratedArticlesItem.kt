package com.example.misobo.mind.items

import com.example.misobo.R
import com.example.misobo.blogs.BlogsModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.curated_articles_item_layout.view.*

class CuratedArticlesItem(
    val onCLick: (BlogsModel.Blogs) -> Unit,
    val viewAllClicked: () -> Unit
) : Item() {
    val adapter = GroupAdapter<ViewHolder>()
    lateinit var blogsModel: BlogsModel

    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.curatedArticlesRecyclerView.adapter = adapter
        adapter.clear()
        val blogSection = Section()
        if (this::blogsModel.isInitialized)
            blogsModel.data?.forEachIndexed { index, model ->
                blogSection.add(ArticlesRecyclerItem(model) {
                    onCLick.invoke(model)
                })
            }
        viewHolder.itemView.viewAll.setOnClickListener { viewAllClicked.invoke() }
        adapter.add(blogSection)
    }

    fun update(blogsModel: BlogsModel) {
        this.blogsModel = blogsModel
        notifyChanged()
    }

    override fun getLayout(): Int = R.layout.curated_articles_item_layout
}