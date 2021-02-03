package com.example.misohe.utils

import com.xwray.groupie.ExpandableGroup

class ToggleGroup : ExpandableGroup(EmptyGroup()) {

    fun setVisibility(visible: Boolean) {
        if (visible) {
            if (!isExpanded)
                onToggleExpanded()
        } else {
            if (isExpanded)
                onToggleExpanded()
        }
    }

    fun hide() {
        if (isExpanded)
            onToggleExpanded()
    }

    fun show() {
        if (!isExpanded)
            onToggleExpanded()
    }
}