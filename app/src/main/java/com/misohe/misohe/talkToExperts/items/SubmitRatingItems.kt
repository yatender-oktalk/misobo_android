package com.misohe.misohe.talkToExperts.items

import android.view.View
import com.bumptech.glide.Glide
import com.misohe.misohe.R
import com.misohe.misohe.talkToExperts.models.UserBookings
import com.misohe.misohe.utils.Util
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.submit_ratings_item.view.*

class SubmitRatingItems(val it: UserBookings.Entry, val submitRating: (Int) -> Unit) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        var userRating = 0
        viewHolder.itemView.expertNameTextView.text = Util.toTitleCase(it.expert?.name ?: "")

        viewHolder.itemView.submitRatingText.setOnClickListener {
            submitRating.invoke(userRating)
            if (userRating != 0) {
                viewHolder.itemView.submittedGroup.visibility = View.VISIBLE
                viewHolder.itemView.submitRatingText.visibility = View.GONE
            }
        }

        viewHolder.itemView.star1.setOnClickListener {
            userRating = 1
            viewHolder.itemView.submitRatingText.text = "Submit Rating"
            Glide.with(viewHolder.containerView.context).load(R.drawable.ratings_rated_star)
                .into(viewHolder.itemView.star1)
            Glide.with(viewHolder.containerView.context).load(R.drawable.rating_unrated)
                .into(viewHolder.itemView.star2)
            Glide.with(viewHolder.containerView.context).load(R.drawable.rating_unrated)
                .into(viewHolder.itemView.star3)
            Glide.with(viewHolder.containerView.context).load(R.drawable.rating_unrated)
                .into(viewHolder.itemView.star4)
            Glide.with(viewHolder.containerView.context).load(R.drawable.rating_unrated)
                .into(viewHolder.itemView.star5)
        }
        viewHolder.itemView.star2.setOnClickListener {
            userRating = 2
            viewHolder.itemView.submitRatingText.text = "Submit Rating"
            Glide.with(viewHolder.containerView.context).load(R.drawable.ratings_rated_star)
                .into(viewHolder.itemView.star1)
            Glide.with(viewHolder.containerView.context).load(R.drawable.ratings_rated_star)
                .into(viewHolder.itemView.star2)
            Glide.with(viewHolder.containerView.context).load(R.drawable.rating_unrated)
                .into(viewHolder.itemView.star3)
            Glide.with(viewHolder.containerView.context).load(R.drawable.rating_unrated)
                .into(viewHolder.itemView.star4)
            Glide.with(viewHolder.containerView.context).load(R.drawable.rating_unrated)
                .into(viewHolder.itemView.star5)

        }
        viewHolder.itemView.star3.setOnClickListener {
            userRating = 3
            viewHolder.itemView.submitRatingText.text = "Submit Rating"
            Glide.with(viewHolder.containerView.context).load(R.drawable.ratings_rated_star)
                .into(viewHolder.itemView.star1)
            Glide.with(viewHolder.containerView.context).load(R.drawable.ratings_rated_star)
                .into(viewHolder.itemView.star2)
            Glide.with(viewHolder.containerView.context).load(R.drawable.ratings_rated_star)
                .into(viewHolder.itemView.star3)
            Glide.with(viewHolder.containerView.context).load(R.drawable.rating_unrated)
                .into(viewHolder.itemView.star4)
            Glide.with(viewHolder.containerView.context).load(R.drawable.rating_unrated)
                .into(viewHolder.itemView.star5)
        }

        viewHolder.itemView.star4.setOnClickListener {
            userRating = 4
            viewHolder.itemView.submitRatingText.text = "Submit Rating"
            Glide.with(viewHolder.containerView.context).load(R.drawable.ratings_rated_star)
                .into(viewHolder.itemView.star1)
            Glide.with(viewHolder.containerView.context).load(R.drawable.ratings_rated_star)
                .into(viewHolder.itemView.star2)
            Glide.with(viewHolder.containerView.context).load(R.drawable.ratings_rated_star)
                .into(viewHolder.itemView.star3)
            Glide.with(viewHolder.containerView.context).load(R.drawable.ratings_rated_star)
                .into(viewHolder.itemView.star4)
            Glide.with(viewHolder.containerView.context).load(R.drawable.rating_unrated)
                .into(viewHolder.itemView.star5)
        }
        viewHolder.itemView.star5.setOnClickListener {
            userRating = 5
            viewHolder.itemView.submitRatingText.text = "Submit Rating"
            Glide.with(viewHolder.containerView.context).load(R.drawable.ratings_rated_star)
                .into(viewHolder.itemView.star1)
            Glide.with(viewHolder.containerView.context).load(R.drawable.ratings_rated_star)
                .into(viewHolder.itemView.star2)
            Glide.with(viewHolder.containerView.context).load(R.drawable.ratings_rated_star)
                .into(viewHolder.itemView.star3)
            Glide.with(viewHolder.containerView.context).load(R.drawable.ratings_rated_star)
                .into(viewHolder.itemView.star4)
            Glide.with(viewHolder.containerView.context).load(R.drawable.ratings_rated_star)
                .into(viewHolder.itemView.star5)
        }
    }

    override fun getLayout(): Int = R.layout.submit_ratings_item
}