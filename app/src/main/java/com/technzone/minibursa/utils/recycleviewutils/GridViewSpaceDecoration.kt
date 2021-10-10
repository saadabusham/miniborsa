package com.technzone.minibursa.utils.recycleviewutils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


class GridViewSpaceDecoration(private val startSpace : Int ,private val endSpace: Int,private val topSpace: Int,private val bottomSpace: Int) : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.left = startSpace
        outRect.right = endSpace
        outRect.top = topSpace
        outRect.bottom = bottomSpace
    }
}