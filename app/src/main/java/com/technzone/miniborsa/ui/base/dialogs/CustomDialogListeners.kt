package com.technzone.miniborsa.ui.base.dialogs

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface

abstract class CustomDialogListeners {

    abstract fun onPositiveClickListener(fragment: CustomDialogFragment?)

    open fun onNegativeClickListener(fragment: CustomDialogFragment?) {}

    open fun onNeutralClickListener(fragment: CustomDialogFragment?) {}

    open fun onBuildListener(context: Context?) {}

    open fun onBuildDoneListener(dialog: Dialog?) {}

    open fun onCancelListener(dialog: DialogInterface?) {}

}