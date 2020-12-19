package com.example.misobo.talkToExperts.items

import android.view.View
import com.bumptech.glide.Glide
import com.example.misobo.R
import com.example.misobo.talkToExperts.models.UserBookings
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.submit_ratings_item.view.*

class SubmitRatingItems(val it: UserBookings.Entry, val submitRating: (Int) -> Unit) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        var userRating = 0
        viewHolder.itemView.expertNameTextView.text = it.expert?.name

        viewHolder.itemView.bookCallAgainText.setOnClickListener {
            submitRating.invoke(userRating)
            viewHolder.itemView.submittedGroup.visibility = View.VISIBLE
            viewHolder.itemView.bookCallAgainText.visibility = View.GONE
        }

        viewHolder.itemView.star1.setOnClickListener {
            userRating = 1
            viewHolder.itemView.bookCallAgainText.text = "Submit Rating"
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
            viewHolder.itemView.bookCallAgainText.text = "Submit Rating"
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
            viewHolder.itemView.bookCallAgainText.text = "Submit Rating"
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
            viewHolder.itemView.bookCallAgainText.text = "Submit Rating"
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
            viewHolder.itemView.bookCallAgainText.text = "Submit Rating"
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