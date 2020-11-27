package com.example.misobo.mind.items

import androidx.core.content.ContextCompat
import com.example.misobo.R
import com.example.misobo.utils.SharedPreferenceManager
import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.unlock_pack_home_banner.view.*

class UnlockItems() : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        when (SharedPreferenceManager.isMindUnlocked()) {
            true -> {
                viewHolder.itemView.packTitle.text = "Body pack"
                viewHolder.itemView.detailsText.text = "Daily Wellness Reminders"
                viewHolder.itemView.rootLayout.background = ContextCompat.getDrawable(
                    viewHolder.containerView.context,
                    R.drawable.unlock_body_mini
                )
                Picasso.get().load(R.drawable.ic_group_434).into(viewHolder.itemView.packIcon)
            }
            false -> {
                viewHolder.itemView.packTitle.text = "Mind pack"
                viewHolder.itemView.detailsText.text = "Daily Wellness Reminders"
                viewHolder.itemView.rootLayout.background = ContextCompat.getDrawable(
                    viewHolder.containerView.context,
                    R.drawable.unlock_mind_mini
                )
                Picasso.get().load(R.drawable.ic_mind_pack_icon).into(viewHolder.itemView.packIcon)
            }
        }
    }

    override fun getLayout(): Int = R.layout.unlock_pack_home_banner
}