package com.technzone.miniborsa.ui.base.views

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import com.google.android.material.textview.MaterialTextView


class AppTextView @JvmOverloads constructor(
        context: Context,
        private var attrs: AttributeSet,
        defStyleAttr: Int = android.R.attr.textViewStyle
) : MaterialTextView(context, attrs, defStyleAttr) {

    private var mText: CharSequence? = null
    private var mIndex = 0
    private var mDelay: Long = 70 //Default 150ms delay


    private val mHandler = Handler()
    private val characterAdder: Runnable = object : Runnable {
        override fun run() {
            text = mText?.subSequence(0, mIndex++)
            if (mIndex <= mText?.length?:0) {
                mHandler.postDelayed(this, mDelay)
            }
        }
    }

    fun animateText(text: CharSequence) {
        mText = text
        mIndex = 0
        setText("")
        mHandler.removeCallbacks(characterAdder)
        mHandler.postDelayed(characterAdder, mDelay)
    }

    fun setCharacterDelay(millis: Long) {
        mDelay = millis
    }
}