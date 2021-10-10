package com.technzone.minibursa.ui.base.dialogs.progressdialog

import android.app.ProgressDialog
import android.content.Context
import android.text.TextUtils
import com.technzone.minibursa.R

object ProgressDialogUtil {

    private var loadingProgressDialog: ProgressDialog? = null

    fun getLoadingDialog(context: Context): ProgressDialog {
        if (loadingProgressDialog == null) {
            loadingProgressDialog = ProgressDialog(context)
        }

        return loadingProgressDialog!!
    }

    fun showLoadingView(context: Context) {
        showProgressDialog(context = context, message = context.getString(R.string.loading))
    }

    fun hideLoadingView(context: Context) {
        if (getLoadingDialog(context).isShowing && loadingProgressDialog !=null) {
            getLoadingDialog(context).dismiss()
        }
    }

    fun showProgressDialog(
        context: Context?,
        title: String = "",
        message: String,
        isRemovable: Boolean = false
    ) {
        if (context == null) return

        if (getLoadingDialog(context).isShowing) {
            return
        }

        getLoadingDialog(context).setMessage(message)
        if (!TextUtils.isEmpty(title)) {
            getLoadingDialog(context).setTitle(title)
        }
        getLoadingDialog(context).setCancelable(isRemovable)
        getLoadingDialog(context).setCanceledOnTouchOutside(isRemovable)

        try {
            getLoadingDialog(context).show()
        } catch (e: Exception) {

        }
    }
}