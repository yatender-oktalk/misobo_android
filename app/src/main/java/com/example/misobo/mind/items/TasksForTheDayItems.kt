package com.example.misobo.mind.items

import com.example.misobo.R
import com.example.misobo.mind.models.MusicResponseModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.tasks_for_the_day_layout.view.*

class TasksForTheDayItems(val musicEntries: MusicResponseModel) : Item() {
    val adapter = GroupAdapter<ViewHolder>()
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.musicRecycler.adapter = adapter
        adapter.clear()
        val musicSection = Section()
        musicEntries.entries?.forEach { model->
            musicSection.add(SongsRecyclerItem(model))
        }
        adapter.add(musicSection)
    }

    override fun getLayout(): Int = R.layout.tasks_for_the_day_layout
}