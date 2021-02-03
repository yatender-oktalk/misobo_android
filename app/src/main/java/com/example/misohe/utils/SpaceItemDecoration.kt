package com.example.misohe.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class SpacesItemDecoration(private val spacing: Int,private val spanCount:Int) : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position: Int = parent.getChildAdapterPosition(view) // item position
        val column: Int = position % spanCount // item column

        outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
        outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

        if (position < spanCount) { // top edge
            outRect.top = spacing;
        }
        outRect.bottom = spacing;

       /* outRect.left = space
        outRect.right = space
        outRect.bottom = space*/

        /*// Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view!!) == 0 || parent.getChildLayoutPosition(view!!) == 1) {
            outRect.top = space
        } else {
            outRect.top = 0
        }*/
    }
}