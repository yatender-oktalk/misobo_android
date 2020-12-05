package com.example.misobo.mind.items

import android.util.Log
import com.example.misobo.R
import com.example.misobo.mind.models.MusicResponseModel
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.music_recycler_item.view.*

class SongsRecyclerItem(val model: MusicResponseModel.MusicModel,val onClick:(Int)->Unit) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.coins.text = model.karma.toString()
        viewHolder.itemView.musicNameText.text = model.title.toString()
        viewHolder.itemView.productionName.text = model.productionName.toString()
        viewHolder.itemView.musicTime.text = "${model.duration?.div(60)} min"

        viewHolder.itemView.playButton.setOnClickListener {
            onClick.invoke(position)
        }

        val progress =model.progress?.toFloat()?.div(model.duration?.toFloat()?:0f)?.times(100)
        Log.i("progress",progress?.toInt().toString())
        viewHolder.itemView.progressBar.setProgress(progress?.toInt()?:0,true)
    }

    override fun getLayout(): Int = R.layout.music_recycler_item
}