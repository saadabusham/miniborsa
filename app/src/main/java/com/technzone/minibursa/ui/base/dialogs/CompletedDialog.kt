package com.technzone.minibursa.ui.base.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.technzone.minibursa.R
import com.technzone.minibursa.databinding.DialogCompletedBinding

class CompletedDialog(context: Context, val icon: Int = R.drawable.ic_completed,
                      val title: String = context.resources.getString(R.string.completed_successfully)) :
        Dialog(context) {

    private lateinit var dialogCompletedDialog: DialogCompletedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawableResource(android.R.color.transparent);
        dialogCompletedDialog =
                DataBindingUtil.inflate(
                        LayoutInflater.from(context),
                        R.layout.dialog_completed,
                        null,
                        false
                )
        dialogCompletedDialog.dialog = this
        setContentView(dialogCompletedDialog.root)
        setCancelable(false)
        dismissTimer.start()
    }

    private val dismissTimer = object : CountDownTimer(3000, 3000) {
        override fun onFinish() {
            dismiss()
        }

        override fun onTick(millisUntilFinished: Long) {
        }

    }

}
