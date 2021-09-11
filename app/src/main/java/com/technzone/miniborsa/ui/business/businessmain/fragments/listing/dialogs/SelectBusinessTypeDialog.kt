package com.technzone.miniborsa.ui.business.businessmain.fragments.listing.dialogs

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.enums.BusinessTypeEnums
import com.technzone.miniborsa.databinding.DialogSelectBusinessTypeBinding


class SelectBusinessTypeDialog(
    var context: Activity,
    val callBack: CallBack
) :
    Dialog(context) {

    lateinit var binding: DialogSelectBusinessTypeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(getContext()),
            R.layout.dialog_select_business_type,
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

    fun onForSaleClicked() {
        callBack.callBack(BusinessTypeEnums.BUSINESS_FOR_SALE)
        dismiss()
    }
    fun onForShareClicked() {
        callBack.callBack(BusinessTypeEnums.BUSINESS_FOR_SHARE)
        dismiss()
    }
    fun onFranchiseClicked() {
        callBack.callBack(BusinessTypeEnums.BUSINESS_FRANCHISE)
        dismiss()
    }

    fun onCloseClicked() {
        dismiss()
    }

    interface CallBack {
        fun callBack(businessTypeEnums: BusinessTypeEnums)
    }
}