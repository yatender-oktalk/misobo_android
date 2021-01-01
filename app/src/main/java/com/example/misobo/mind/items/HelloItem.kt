package com.example.misobo.mind.items

import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import com.example.misobo.R
import com.example.misobo.utils.SharedPreferenceManager
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.hello_item_layout.view.*


class HelloItem(val name: String?, val onClick: (String) -> Unit) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {

        if (name != null) {
            viewHolder.itemView.editNameGroup.visibility = View.GONE
            viewHolder.itemView.nameTextView.text = name
            viewHolder.itemView.nameTextView.visibility = View.VISIBLE
        } else {
            viewHolder.itemView.nameTextView.visibility = View.GONE
            viewHolder.itemView.editNameGroup.visibility = View.VISIBLE
        }

        viewHolder.itemView.karmaCoinsText.text =
            SharedPreferenceManager.getUserProfile()?.data?.karmaPoints

        viewHolder.itemView.editName.setOnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode === KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                if (!viewHolder.itemView.editName.text.isNullOrEmpty())
                    onClick.invoke(viewHolder.itemView.editName.text.toString())
            }
            false
        }

    }

    override fun getLayout(): Int = R.layout.hello_item_layout
}