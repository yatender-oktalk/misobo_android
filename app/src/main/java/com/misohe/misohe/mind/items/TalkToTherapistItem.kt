package com.misohe.misohe.mind.items

import com.misohe.misohe.R
import com.misohe.misohe.talkToExperts.items.ExpertsRecyclerItem
import com.misohe.misohe.talkToExperts.models.ExpertModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.talk_to_therapist_item.view.*

class TalkToTherapistItem(val expertList: ExpertModel, val exploreAllClick: () -> Unit,val callClicked:(ExpertModel.Expert)->Unit) : Item() {
    val adapter = GroupAdapter<ViewHolder>()

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.expertsRecyclerView.adapter = adapter
        adapter.clear()
        val expertsSection = Section()
        expertList.entries?.take(3)?.forEach { model ->
            expertsSection.add(ExpertsRecyclerItem(model) {
                callClicked.invoke(model)

            })
        }
        viewHolder.itemView.exploreMore.setOnClickListener { exploreAllClick.invoke() }
        adapter.add(expertsSection)
    }

    override fun getLayout(): Int = R.layout.talk_to_therapist_item
}