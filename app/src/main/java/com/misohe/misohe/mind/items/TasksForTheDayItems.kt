package com.misohe.misohe.mind.items

import androidx.paging.PagedList
import com.misohe.misohe.R
import com.misohe.misohe.mind.models.MusicResponseModel
import com.misohe.misohe.mind.pagination.MusicListAdapter
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.tasks_for_the_day_layout.view.*

class TasksForTheDayItems(
    val onClick: (MusicResponseModel.MusicModel) -> Unit,
    val viewAllClick: () -> Unit
) : Item() {
    //private lateinit var musicListAdapter: MusicListAdapter
    lateinit var musicEntries: PagedList<MusicResponseModel.MusicModel>

    val musicListAdapter = MusicListAdapter() { musicModel, position ->
        musicModel?.let { onClick.invoke(it) }
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.musicRecycler.adapter = musicListAdapter
        if (this::musicEntries.isInitialized) {
            musicListAdapter.submitList(musicEntries)

            /* if (musicEntries.isNullOrEmpty()) {
                 viewHolder.itemView.root.visibility = View.GONE
             } else {
                 viewHolder.itemView.root.visibility = View.VISIBLE
             }*/
        }

        viewHolder.itemView.viewAll.setOnClickListener {
            viewAllClick.invoke()
        }
    }

    fun update(musicEntries: PagedList<MusicResponseModel.MusicModel>) {
        this.musicEntries = musicEntries
        notifyChanged()
    }

    override fun getLayout(): Int = R.layout.tasks_for_the_day_layout
}