package com.example.misobo.talkToExperts.pagination

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.misobo.R
import com.example.misobo.talkToExperts.models.ExpertModel
import com.example.misobo.utils.Util
import kotlinx.android.synthetic.main.experts_recycler_item.view.*

class ExpertsListAdapter(private val callClicked: (ExpertModel.Expert?) -> Unit) :
    PagedListAdapter<ExpertModel.Expert, RecyclerView.ViewHolder>(
        diffCallback
    ) {

    companion object {

        val diffCallback = object : DiffUtil.ItemCallback<ExpertModel.Expert>() {
            override fun areItemsTheSame(
                oldItem: ExpertModel.Expert,
                newItem: ExpertModel.Expert
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ExpertModel.Expert,
                newItem: ExpertModel.Expert
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflator = LayoutInflater.from(parent.context)
        return ExpertsViewHolder(
            layoutInflator.inflate(
                R.layout.experts_recycler_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        //inflateView(holder, position)
        viewHolder.itemView.expertName.text = Util.toTitleCase(getItem(position)?.name?:"")
        viewHolder.itemView.coinsNeeded.text = "${getItem(position)?.karmaCoinsNeeded ?: 0}/30 Min"

        viewHolder.itemView.expertCategory.text =
            getItem(position)?.qualification ?: ""
        if (getItem(position)?.qualification?.length ?: 0 > 24) {
            viewHolder.itemView.expertCategory.text =
                getItem(position)?.qualification?.substring(0, 24) + "...";
        }
        viewHolder.itemView.expertLanguage.text = getItem(position)?.language
        viewHolder.itemView.expertStar.text = getItem(position)?.rating ?: "4.5"
        viewHolder.itemView.customers.text = "${getItem(position)?.consultations ?: 432}"
        viewHolder.itemView.experienceTextView.text =
            "Exp ${getItem(position)?.experience ?: 12} year"
        viewHolder.itemView.setOnClickListener {
            callClicked.invoke(getItem(position))
        }
        Glide.with(viewHolder.itemView.context).load(getItem(position)?.image)
            .into(viewHolder.itemView.expertImage)

        if (getItem(position)?.expertise != null) {
            viewHolder.itemView.expertiseGroup.visibility = View.VISIBLE
            viewHolder.itemView.expertise.text = getItem(position)?.expertise
        } else {
            viewHolder.itemView.expertiseGroup.visibility = View.GONE
        }
    }

    private fun inflateView(viewHolder: RecyclerView.ViewHolder, position: Int) {

    }

    inner class ExpertsViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
