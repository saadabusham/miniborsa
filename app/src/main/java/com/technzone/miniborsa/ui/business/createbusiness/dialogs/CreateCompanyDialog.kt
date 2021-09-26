package com.technzone.miniborsa.ui.business.createbusiness.dialogs

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.technzone.miniborsa.R
import com.technzone.miniborsa.databinding.DialogCreateCompanyBinding
import com.technzone.miniborsa.utils.extensions.showErrorAlert

class CreateCompanyDialog(
    private var context: Activity,
    private val companyNameCallBack: CompanyNameCallBack?
) :
    Dialog(context) {

    lateinit var binding: DialogCreateCompanyBinding

    val companyName: MutableLiveData<String> = MutableLiveData("")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(getContext()),
            R.layout.dialog_create_company,
            null,
            false
        ); binding.viewModel = this
        setContentView(binding.root)
        window?.setBackgroundDrawableResource(android.R.color.transparent);
    }

    fun onCancelClicked() {
        dismiss()
    }

    fun onSaveClicked() {
        if (companyName.value.isNullOrEmpty()) {
            context.showErrorAlert(
                context.resources.getString(R.string.company_name),
                context.resources.getString(R.string.please_enter_a_valid_company_name)
            )
            return
        } else {
            dismiss()
            companyNameCallBack?.save(companyName.value.toString(),this)
        }
    }

    interface CompanyNameCallBack {
        fun save(name: String,dialog:CreateCompanyDialog)
    }
}