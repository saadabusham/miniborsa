package com.technzone.minibursa.ui.business.listingpreview.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.technzone.minibursa.R
import com.technzone.minibursa.databinding.DialogSubmitedBinding

class SubmittedDialogFragment(private val mContext: Context) :
    Dialog(mContext, R.style.FailedDialog) {

    private lateinit var binding: DialogSubmitedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.dialog_submited,
                null,
                false
            )
        setContentView(binding.root)
        setCancelable(false)
        binding.btnContinue.setOnClickListener {
            dismiss()
        }
    }

}