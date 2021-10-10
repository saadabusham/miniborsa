package com.technzone.minibursa.ui.base.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.technzone.minibursa.R
import com.technzone.minibursa.databinding.ProgressbarBinding

open class CustomDialogUtils : Dialog {
    private var mProgressbar: CustomDialogUtils? = null

    private constructor(context: Context) : super(context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogProgressBinding: ProgressbarBinding = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()),
                R.layout.progressbar,
                null,
                false
        )
        setContentView(dialogProgressBinding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    constructor(context: Context, withProgress: Boolean = true, isShowNow: Boolean = true) : super(
            context
    ) {
        if (withProgress) {
            if (mProgressbar == null)
                mProgressbar = CustomDialogUtils(context)
            if (isShowNow) showProgress()
        }
    }

    fun showProgress() {
        if (mProgressbar != null && mProgressbar!!.isShowing) {
            mProgressbar!!.cancel()
        }
        mProgressbar!!.setCancelable(false)
        mProgressbar!!.show()
    }

    fun hideProgress() {
        if (mProgressbar != null)
            mProgressbar!!.dismiss()
    }
}