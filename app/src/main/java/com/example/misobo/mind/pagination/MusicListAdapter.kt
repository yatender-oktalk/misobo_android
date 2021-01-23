package com.example.misobo.mind.pagination

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.misobo.R
import com.example.misobo.mind.models.MusicResponseModel
import kotlinx.android.synthetic.main.music_recycler_item.view.*

class MusicListAdapter(private val onClick: (MusicResponseModel.MusicModel?, Int) -> Unit) :
    PagedListAdapter<MusicResponseModel.MusicModel, RecyclerView.ViewHolder>(
        diffCallback
    ) {

    companion object {

        val diffCallback = object : DiffUtil.ItemCallback<MusicResponseModel.MusicModel>() {
            override fun areItemsTheSame(
                oldItem: MusicResponseModel.MusicModel,
                newItem: MusicResponseModel.MusicModel
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: MusicResponseModel.MusicModel,
                newItem: MusicResponseModel.MusicModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflator = LayoutInflater.from(parent.context)
        return MusicViewHolder(layoutInflator.inflate(R.layout.music_recycler_item, parent, false))
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        viewHolder.itemView.coins.text = getItem(position)?.karma.toString()
        viewHolder.itemView.musicNameText.text = getItem(position)?.title.toString()
        viewHolder.itemView.productionName.text = getItem(position)?.productionName.toString()
        viewHolder.itemView.musicTime.text = "${getItem(position)?.duration?.div(60)} min"
        viewHolder.itemView.soul.text = "${getItem(position)?.tag}"
        Glide.with(viewHolder.itemView.context).load(getItem(position)?.image)
            .placeholder(R.drawable.music_gradient).into(viewHolder.itemView.musicCardBackground)

        viewHolder.itemView.playButton.setOnClickListener {
            onClick.invoke(getItem(position), position)
        }

        if (position == 0)
            viewHolder.itemView.rootLayout.background =
                ContextCompat.getDrawable(viewHolder.itemView.context, R.drawable.music_gradient)
        else if (position == 1)
            viewHolder.itemView.rootLayout.background =
                ContextCompat.getDrawable(viewHolder.itemView.context, R.drawable.gradient_2)

        val progress = getItem(position)?.progress?.toFloat()
            ?.div(getItem(position)?.duration?.toFloat() ?: 0f)?.times(100)

        if (progress?.toInt()!! > 0)
            viewHolder.itemView.progressBar.setProgress(progress?.toInt() ?: 0, true)
        else {
            viewHolder.itemView.progressBar.visibility = View.GONE
        }
    }

    inner class MusicViewHolder(view: View) : RecyclerView.ViewHolder(view)

}
