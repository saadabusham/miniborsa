package com.technzone.minibursa.ui.base.views.listeners

import android.os.SystemClock
import android.view.View
import java.util.*
import kotlin.math.abs

abstract class SingleClickListener(private val minimumIntervalMillis: Long = 600L) : View.OnClickListener {
    private var lastClickMap: MutableMap<View, Long>? = null

    init {
        lastClickMap = WeakHashMap()
    }

    /**
     * Implement this in your subclass instead of onClick
     * @param v The view that was clicked
     */
    abstract fun onSingleClick(v: View?)

    override fun onClick(clickedView: View) {
        val previousClickTimestamp = lastClickMap!![clickedView]
        val currentTimestamp = SystemClock.uptimeMillis()
        lastClickMap!![clickedView] = currentTimestamp
        if (previousClickTimestamp == null || abs(currentTimestamp - previousClickTimestamp) > minimumIntervalMillis) {
            onSingleClick(clickedView)
        }
    }

}