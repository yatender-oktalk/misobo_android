package com.example.misobo.mind.items

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.misobo.R
import com.example.misobo.mind.models.MusicResponseModel
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.music_recycler_item.view.*
import java.lang.Exception

class SongsRecyclerItem(val model: MusicResponseModel.MusicModel,val onClick:()->Unit) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.coins.text = model.karma.toString()
        viewHolder.itemView.musicNameText.text = model.title.toString()
        viewHolder.itemView.productionName.text = model.productionName.toString()
        viewHolder.itemView.musicTime.text = "${model.duration?.div(60)} min"

        viewHolder.itemView.playButton.setOnClickListener {
            onClick.invoke()
        }
        viewHolder.itemView.progress.progress = 20

    }

    override fun getLayout(): Int = R.layout.music_recycler_item
}