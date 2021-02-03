package com.example.misohe.mind.items

import androidx.core.content.ContextCompat
import com.example.misohe.R
import com.example.misohe.utils.SharedPreferenceManager
import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.unlock_pack_home_banner.view.*

class UnlockItems(val navigateToBody: (Boolean) -> Unit) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        when (SharedPreferenceManager.getUserProfile()?.data?.isMindPackUnlocked) {
            true -> {
                viewHolder.itemView.packTitle.text = "Soul & Heal Pack"
                viewHolder.itemView.detailsText.text = "Daily Wellness Reminders"
                viewHolder.itemView.rootLayout.background = ContextCompat.getDrawable(
                    viewHolder.containerView.context,
                    R.drawable.unlock_body_mini
                )
                Picasso.get().load(R.drawable.ic_group_434).into(viewHolder.itemView.packIcon)
                viewHolder.itemView.setOnClickListener { navigateToBody.invoke(true) }

            }
            false -> {
                viewHolder.itemView.packTitle.text = "Mind pack"
                viewHolder.itemView.detailsText.text = "Daily Wellness Reminders"
                viewHolder.itemView.rootLayout.background = ContextCompat.getDrawable(
                    viewHolder.containerView.context,
                    R.drawable.unlock_mind_mini
                )
                Picasso.get().load(R.drawable.ic_mind_pack_icon).into(viewHolder.itemView.packIcon)
                viewHolder.itemView.setOnClickListener { navigateToBody.invoke(false) }
            }
        }
    }

    override fun getLayout(): Int = R.layout.unlock_pack_home_banner
}