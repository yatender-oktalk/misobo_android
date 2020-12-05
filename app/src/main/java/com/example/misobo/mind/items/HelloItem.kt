package com.example.misobo.mind.items

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import com.example.misobo.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.hello_item_layout.view.*


class HelloItem(val onClick: (String) -> Unit) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {

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