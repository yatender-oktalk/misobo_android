package com.example.misobo.talkToExperts.items

import com.example.misobo.R
import com.example.misobo.talkToExperts.models.ExpertModel
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.experts_recycler_item.view.*

class ExpertsRecyclerItem(val it: ExpertModel.Expert, val callClicked: () -> Unit) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.expertName.text = it.name
        viewHolder.itemView.coinsNeeded.text = "${it.karmaCoinsNeeded ?: 0}/Min"

        viewHolder.itemView.expertCategory.text = "Vedic Astrologer"
        viewHolder.itemView.expertLanguage.text = it.language
        viewHolder.itemView.expertStar.text = it.rating ?: "4.5"
        viewHolder.itemView.customers.text = "${it.consultations ?: 432}"
        viewHolder.itemView.experienceTextView.text = "Exp ${it.experience?:12} year"
        viewHolder.itemView.callButton.setOnClickListener {
            callClicked.invoke()
        }
    }

    override fun getLayout(): Int = R.layout.experts_recycler_item
}