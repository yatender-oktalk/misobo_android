package com.example.misobo.mind.items

import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.example.misobo.R
import com.example.misobo.mind.models.MusicResponseModel
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.music_recycler_item.view.*

class SongsRecyclerItem(
    private val model: MusicResponseModel.MusicModel,
    val onClick: (Int) -> Unit
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.coins.text = model.karma.toString()
        viewHolder.itemView.musicNameText.text = model.title.toString()
        viewHolder.itemView.productionName.text = model.productionName.toString()
        viewHolder.itemView.musicTime.text = "${model.duration?.div(60)} min"
        viewHolder.itemView.soul.text = "${model.tag}"

        viewHolder.itemView.playButton.setOnClickListener {
            onClick.invoke(position)
        }

        if (position == 0)
            viewHolder.itemView.rootLayout.background =
                ContextCompat.getDrawable(viewHolder.itemView.context, R.drawable.music_gradient)
        else if (position == 1)
            viewHolder.itemView.rootLayout.background =
                ContextCompat.getDrawable(viewHolder.itemView.context, R.drawable.gradient_2)

        val progress = model.progress?.toFloat()?.div(model.duration?.toFloat() ?: 0f)?.times(100)

        if (progress?.toInt()!! > 0)
            viewHolder.itemView.progressBar.setProgress(progress?.toInt() ?: 0, true)
        else {
            viewHolder.itemView.progressBar.visibility = View.GONE
        }
    }

    override fun getLayout(): Int = R.layout.music_recycler_item
}