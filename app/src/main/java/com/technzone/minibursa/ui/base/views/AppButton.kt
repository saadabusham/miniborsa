package com.technzone.minibursa.ui.base.views

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.view.MotionEvent
import com.google.android.material.button.MaterialButton
import com.technzone.minibursa.R


class AppButton @JvmOverloads constructor(
        context: Context,
        private val attrs: AttributeSet? = null,
        defStyleAttr: Int = R.attr.materialButtonStyle
) : MaterialButton(context, attrs, defStyleAttr) {

    var lastClickTime: Long = 0

    init {
        initAttrs()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        return if (event?.action != MotionEvent.ACTION_UP || isValid)
            super.onTouchEvent(event)
        else {
            event.action = MotionEvent.ACTION_CANCEL
            super.onTouchEvent(event)
        }
    }

    private val isValid: Boolean
        get() {
            return if (lastClickTime == 0.toLong() || SystemClock.elapsedRealtime() - lastClickTime > 1000L) {
                lastClickTime = SystemClock.elapsedRealtime()
                true
            } else false
        }

    private fun initAttrs() {
        val btnAttr = context.obtainStyledAttributes(
                attrs,
                R.styleable.MaterialButton
        )
        btnAttr.recycle()
    }

}