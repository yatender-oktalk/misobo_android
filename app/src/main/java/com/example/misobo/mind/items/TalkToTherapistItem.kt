package com.example.misobo.mind.items

import com.example.misobo.R
import com.example.misobo.talkToExperts.items.ExpertsRecyclerItem
import com.example.misobo.talkToExperts.models.ExpertModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.talk_to_therapist_item.view.*
import kotlinx.android.synthetic.main.tasks_for_the_day_layout.view.*

class TalkToTherapistItem(val expertList: ExpertModel) : Item() {
    val adapter = GroupAdapter<ViewHolder>()

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.expertsRecyclerView.adapter = adapter
        adapter.clear()
        val expertsSection = Section()
        expertList.entries?.take(3)?.forEach { model->
            expertsSection.add(ExpertsRecyclerItem(model){

            })
        }
        adapter.add(expertsSection)
    }

    override fun getLayout(): Int = R.layout.talk_to_therapist_item
}