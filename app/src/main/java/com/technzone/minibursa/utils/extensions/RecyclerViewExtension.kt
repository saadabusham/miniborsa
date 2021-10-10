package com.technzone.minibursa.utils.extensions

import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.technzone.minibursa.common.CommonEnums
import com.technzone.minibursa.utils.LocaleUtil.Companion.getLanguage

fun getSnapHelper(): LinearSnapHelper? {
    return object : LinearSnapHelper() {
        override fun findTargetSnapPosition(
            layoutManager: RecyclerView.LayoutManager,
            velocityX: Int,
            velocityY: Int
        ): Int {
            val centerView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
            val position = layoutManager.getPosition(centerView)
            var targetPosition = -1
            if (layoutManager.canScrollHorizontally()) {
                targetPosition =
                    if (getLanguage() == CommonEnums.Languages.Arabic.value) {
                        if (velocityX < 0) {
                            position + 1
                        } else {
                            position - 1
                        }
                    } else {
                        if (velocityX < 0) {
                            position - 1
                        } else {
                            position + 1
                        }
                    }
            }
            if (layoutManager.canScrollVertically()) {
                targetPosition = if (velocityY < 0) {
                    position - 1
                } else {
                    position + 1
                }
            }
            val firstItem = 0
            val lastItem = layoutManager.itemCount - 1
            targetPosition =
                Math.min(lastItem, Math.max(targetPosition, firstItem))
            return targetPosition
        }
    }
}