package com.example.misobo.mind.items

import androidx.paging.PagedList
import com.example.misobo.R
import com.example.misobo.mind.models.MusicResponseModel
import com.example.misobo.mind.pagination.MusicListAdapter
import com.example.misobo.talkToExperts.pagination.BookingsListAdapter
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.tasks_for_the_day_layout.view.*

class TasksForTheDayItems(
    val musicEntries: PagedList<MusicResponseModel.MusicModel>,
    val onClick: (Int) -> Unit
) : Item() {
    private lateinit var musicListAdapter: MusicListAdapter

    override fun bind(viewHolder: ViewHolder, position: Int) {
        musicListAdapter = MusicListAdapter() { musicModel, position ->
            onClick.invoke(position)
        }
        viewHolder.itemView.musicRecycler.adapter = musicListAdapter
        musicListAdapter.submitList(musicEntries)

        /* musicEntries?.forEach { model ->
             musicSection.add(SongsRecyclerItem(model) {
                 onClick.invoke(it)
             })
         }*/
    }

    override fun getLayout(): Int = R.layout.tasks_for_the_day_layout
}