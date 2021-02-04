package com.misohe.misohe.talkToExperts.items

import android.view.View
import com.bumptech.glide.Glide
import com.misohe.misohe.R
import com.misohe.misohe.talkToExperts.models.ExpertModel
import com.misohe.misohe.utils.Util
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.experts_recycler_item.view.*

class ExpertsRecyclerItem(val it: ExpertModel.Expert, val callClicked: () -> Unit) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.expertName.text = Util.toTitleCase(it.name?:"")
        viewHolder.itemView.coinsNeeded.text = "${it.karmaCoinsNeeded ?: 0}/30 Min"

        viewHolder.itemView.expertCategory.text = it.qualification ?: ""
        if (it.qualification?.length ?: 0 > 24) {
            viewHolder.itemView.expertCategory.text = it.qualification?.substring(0, 24) + "...";
        }

        viewHolder.itemView.expertLanguage.text = it.language
        viewHolder.itemView.expertStar.text = it.rating ?: "4.5"
        viewHolder.itemView.customers.text = "${it.consultations ?: 432}"
        viewHolder.itemView.experienceTextView.text = "Exp ${it.experience ?: 12} year"
        viewHolder.itemView.setOnClickListener {
            callClicked.invoke()
        }
        if (it.expertise != null) {
            viewHolder.itemView.expertiseGroup.visibility = View.VISIBLE
            viewHolder.itemView.expertise.text = it.expertise
        } else {
            viewHolder.itemView.expertiseGroup.visibility = View.GONE
        }

        Glide.with(viewHolder.itemView.context).load(it.image).into(viewHolder.itemView.expertImage)
    }

    override fun getLayout(): Int = R.layout.experts_recycler_item
}