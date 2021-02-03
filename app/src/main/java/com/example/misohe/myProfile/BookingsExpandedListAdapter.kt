package com.example.misohe.talkToExperts.pagination

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.misohe.R
import com.example.misohe.talkToExperts.models.UserBookings
import com.example.misohe.utils.Util
import kotlinx.android.synthetic.main.submit_ratings_expanded.view.*
import kotlinx.android.synthetic.main.user_bookings_layout.view.coinsNeeded
import kotlinx.android.synthetic.main.user_bookings_layout.view.expertCategory
import kotlinx.android.synthetic.main.user_bookings_layout.view.expertNameTextView
import kotlinx.android.synthetic.main.user_bookings_layout_expanded.view.*
import java.text.SimpleDateFormat
import java.util.*

class BookingsExpandedListAdapter(
    private val submitRating: (UserBookings.Entry?, Int) -> Unit,
    private val needHelpClicked: () -> Unit
) :
    PagedListAdapter<UserBookings.Entry, RecyclerView.ViewHolder>(
        diffCallback
    ) {

    companion object {
        private val RATING_VIEW_TYPE = 0
        private val BOOKINGS_VIEW_TYPE = 1

        val diffCallback = object : DiffUtil.ItemCallback<UserBookings.Entry>() {
            override fun areItemsTheSame(
                oldItem: UserBookings.Entry,
                newItem: UserBookings.Entry
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: UserBookings.Entry,
                newItem: UserBookings.Entry
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflator = LayoutInflater.from(parent.context)
        return when (viewType) {
            RATING_VIEW_TYPE -> {
                RatingViewHolder(
                    layoutInflator.inflate(
                        R.layout.submit_ratings_expanded,
                        parent,
                        false
                    )
                )
            }
            BOOKINGS_VIEW_TYPE -> {
                BookingViewHolder(
                    layoutInflator.inflate(
                        R.layout.user_bookings_layout_expanded,
                        parent,
                        false
                    )
                )
            }
            else -> {
                return SubmittedRatingsViewHolder(
                    layoutInflator.inflate(
                        R.layout.submitted_ratings_layout,
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RatingViewHolder -> {
                inflateView(holder, position)
            }
            is BookingViewHolder -> {
                holder.itemView.expertNameTextView.text =
                    Util.toTitleCase(getItem(position)?.expert?.name ?: "")
                holder.itemView.expertCategory.text =
                    getItem(position)?.expert?.qualification ?: ""

                if (getItem(position)?.expert?.qualification?.length ?: 0 > 20) {
                    holder.itemView.expertCategory.text =
                        getItem(position)?.expert?.qualification?.substring(0, 20) + "...";
                }
                holder.itemView.expertsLanguage.text = getItem(position)?.expert?.language
                holder.itemView.coinsNeeded.text =
                    getItem(position)?.expert?.karmaCoinsNeeded.toString()
                holder.itemView.bookCallAgainText.setOnClickListener {
                    //needHelpClick.invoke()
                    needHelpClicked.invoke()
                }
                Glide.with(holder.itemView.context).load(getItem(position)?.expert?.image)
                    .placeholder(R.color.colorAccent)
                    .into(holder.itemView.expertImages)
            }
            is SubmittedRatingsViewHolder -> {
            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val currentDateTime = Date().time
        val callCompleted =
            currentDateTime.compareTo(dateFormat.parse(getItem(position)?.endTime).time)

        if (callCompleted == 1 && getItem(position)?.isRated == false) {
            return RATING_VIEW_TYPE
        } else if (callCompleted != 1 && getItem(position)?.isRated == false) {
            return BOOKINGS_VIEW_TYPE
        } else {
            return BOOKINGS_VIEW_TYPE
        }
    }

    private fun inflateView(viewHolder: RecyclerView.ViewHolder, position: Int) {
        var userRating = 0
        viewHolder.itemView.expertNameTextView.text =
            Util.toTitleCase(getItem(position)?.expert?.name ?: "")

        viewHolder.itemView.submitRatingText.setOnClickListener {
            submitRating.invoke(getItem(position), userRating)
            if (userRating != 0) {
                this.notifyItemChanged(position)
                viewHolder.itemView.submittedGroup.visibility = View.VISIBLE
                viewHolder.itemView.submitRatingText.visibility = View.GONE
            }
        }

        Glide.with(viewHolder.itemView.context).load(getItem(position)?.expert?.image)
            .placeholder(R.color.colorAccent)
            .into(viewHolder.itemView.expertImage)

        viewHolder.itemView.star1.setOnClickListener {
            userRating = 1
            viewHolder.itemView.submitRatingText.text = "Submit Rating"
            inflateStarView(
                viewHolder,
                R.drawable.ratings_rated_star,
                R.drawable.rating_unrated,
                R.drawable.rating_unrated,
                R.drawable.rating_unrated,
                R.drawable.rating_unrated
            )
        }
        viewHolder.itemView.star2.setOnClickListener {
            userRating = 2
            viewHolder.itemView.submitRatingText.text = "Submit Rating"
            inflateStarView(
                viewHolder,
                R.drawable.ratings_rated_star,
                R.drawable.ratings_rated_star,
                R.drawable.rating_unrated,
                R.drawable.rating_unrated,
                R.drawable.rating_unrated
            )
        }
        viewHolder.itemView.star3.setOnClickListener {
            userRating = 3
            viewHolder.itemView.submitRatingText.text = "Submit Rating"
            inflateStarView(
                viewHolder,
                R.drawable.ratings_rated_star,
                R.drawable.ratings_rated_star,
                R.drawable.ratings_rated_star,
                R.drawable.rating_unrated,
                R.drawable.rating_unrated
            )
        }

        viewHolder.itemView.star4.setOnClickListener {
            userRating = 4
            viewHolder.itemView.submitRatingText.text = "Submit Rating"
            inflateStarView(
                viewHolder,
                R.drawable.ratings_rated_star,
                R.drawable.ratings_rated_star,
                R.drawable.ratings_rated_star,
                R.drawable.ratings_rated_star,
                R.drawable.rating_unrated
            )
        }
        viewHolder.itemView.star5.setOnClickListener {
            userRating = 5
            viewHolder.itemView.submitRatingText.text = "Submit Rating"
            inflateStarView(
                viewHolder,
                R.drawable.ratings_rated_star,
                R.drawable.ratings_rated_star,
                R.drawable.ratings_rated_star,
                R.drawable.ratings_rated_star,
                R.drawable.ratings_rated_star
            )
        }
    }

    private fun inflateStarView(
        viewHolder: RecyclerView.ViewHolder,
        star1: Int,
        star2: Int,
        star3: Int,
        star4: Int,
        star5: Int
    ) {
        Glide.with(viewHolder.itemView.context).load(star1)
            .into(viewHolder.itemView.star1)
        Glide.with(viewHolder.itemView.context).load(star2)
            .into(viewHolder.itemView.star2)
        Glide.with(viewHolder.itemView.context).load(star3)
            .into(viewHolder.itemView.star3)
        Glide.with(viewHolder.itemView.context).load(star4)
            .into(viewHolder.itemView.star4)
        Glide.with(viewHolder.itemView.context).load(star5)
            .into(viewHolder.itemView.star5)
    }

    inner class RatingViewHolder(view: View) : RecyclerView.ViewHolder(view)

    inner class BookingViewHolder(view: View) : RecyclerView.ViewHolder(view)

    inner class SubmittedRatingsViewHolder(view: View) : RecyclerView.ViewHolder(view)

}