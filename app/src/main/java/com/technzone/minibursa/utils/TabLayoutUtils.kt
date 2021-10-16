package com.technzone.minibursa.utils

import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout

object TabLayoutUtils {

    fun TabLayout.enableTabs(enable: Boolean) {
        val viewGroup = this.getTabViewGroup()
        if (viewGroup != null) for (childIndex in 0 until viewGroup.childCount) {
            val tabView = viewGroup.getChildAt(childIndex)
            if (tabView != null) tabView.isEnabled = enable
        }
    }

    fun TabLayout.getTabView(position: Int): View? {
        var tabView: View? = null
        val viewGroup = this.getTabViewGroup()
        if (viewGroup != null && viewGroup.childCount > position) tabView =
            viewGroup.getChildAt(position)
        return tabView
    }

    private fun TabLayout.getTabViewGroup(): ViewGroup? {
        var viewGroup: ViewGroup? = null
        if (this != null && this.childCount > 0) {
            val view = this.getChildAt(0)
            if (view != null && view is ViewGroup) viewGroup = view
        }
        return viewGroup
    }
}