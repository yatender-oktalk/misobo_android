package com.example.misobo.talkToExperts

import com.example.misobo.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.experts_recycler_item.view.*

class ExpertsRecyclerItem(val it: ExpertModel.Expert) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.expertName.text = it.name
        viewHolder.itemView.expertLanguage.text = it.language
        it.rating?.let {
            viewHolder.itemView.expertStar.text = it
        }
        viewHolder.itemView.expertStar.text = it.rating.toString()
        viewHolder.itemView.customers.text = it.consultations.toString()
        it.experience?.let {
            viewHolder.itemView.experienceTextView.text = "Exp $it year"
        }
    }

    override fun getLayout(): Int = R.layout.experts_recycler_item
}