package com.example.misohe.talkToExperts.items

import com.example.misohe.R
import com.example.misohe.talkToExperts.models.Packs
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.packs_item_layout.view.*

class PacksItem(val pack:Packs,val onClick:()->Unit):Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.coins.text = pack.karmaCoins.toString()
        viewHolder.itemView.amount.text = "₹${pack.amount}"
        viewHolder.itemView.setOnClickListener {
            onClick.invoke()
        }
    }

    override fun getLayout(): Int = R.layout.packs_item_layout
}