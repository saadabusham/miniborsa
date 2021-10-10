package com.technzone.minibursa.ui.base.dialogs

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.technzone.minibursa.R
import com.technzone.minibursa.databinding.DialogLoginBinding


class LoginDialog(
    var context: Activity,
    val callBack: CallBack
) :
    Dialog(context) {

    lateinit var binding: DialogLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(getContext()),
            R.layout.dialog_login,
            null,
            false
        );
        binding.viewModel = this
        setContentView(binding.root)
        val lp = WindowManager.LayoutParams()
        val window: Window? = window
        lp.copyFrom(window?.attributes)
        //This makes the dialog take up the full width
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = lp
        window?.setBackgroundDrawableResource(android.R.color.transparent);
    }

    fun onLoginClicked() {
        callBack.callBackLogin()
        dismiss()
    }

    fun onCloseClicked() {
        dismiss()
    }

    interface CallBack {
        fun callBackLogin()
    }
}