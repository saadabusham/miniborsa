package com.technzone.miniborsa.ui.core.changepassword

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.miniborsa.data.common.CustomObserverResponse
import com.technzone.miniborsa.databinding.ActivityChangePasswordBinding
import com.technzone.miniborsa.ui.base.activity.BaseBindingActivity
import com.technzone.miniborsa.ui.base.dialogs.CompletedDialog
import com.technzone.miniborsa.utils.extensions.hideKeyboard
import com.technzone.miniborsa.utils.extensions.showErrorAlert
import com.technzone.miniborsa.utils.extensions.validate
import com.technzone.miniborsa.utils.extensions.validateConfirmPassword
import com.technzone.miniborsa.utils.validation.ValidatorInputTypesEnums
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.layout_toolbar.*


@AndroidEntryPoint
class ChangePasswordActivity : BaseBindingActivity<ActivityChangePasswordBinding>() {

    private val viewModel: ChangePasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            layoutResID = R.layout.activity_change_password,
            hasToolbar = true,
            toolbarView = toolbar,
            hasBackButton = true,
            showBackArrow = true,
            hasTitle = true,
            title = R.string.change_password
        )
        binding?.viewModel = viewModel
        setUpListeners()
    }

    private fun setUpListeners() {
        binding?.btnlogin?.setOnClickListener {
            if (validateInput())
                viewModel.changePassword().observe(this, changePasswordResultObserver())
        }
    }

    private fun changePasswordResultObserver(): CustomObserverResponse<Any> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<Any> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: Any?
                ) {
                    binding?.root.hideKeyboard(this@ChangePasswordActivity)
                    showCompletedDialog()
                }
            })
    }

    private fun showCompletedDialog() {
        val completedDialog = CompletedDialog(
            context = this,
            title = resources.getString(R.string.updated_successfully)
        )
        completedDialog.setOnDismissListener {
            finish()
        }
        completedDialog.show()

    }

    private fun validateInput(): Boolean {
        var valid = true
        binding?.edOldPassword?.text.toString()
            .validate(ValidatorInputTypesEnums.PASSWORD, this).let {
                if (!it.isValid) {
                    showErrorAlert(
                        title = resources.getString(R.string.old_password),
                        message = it.errorMessage
                    )
                    return false
                }
            }
        binding?.edNewPassword?.text.toString()
            .validate(ValidatorInputTypesEnums.PASSWORD, this).let {
                if (!it.isValid) {
                    showErrorAlert(
                        title = resources.getString(R.string.new_password),
                        message = it.errorMessage
                    )
                    return false
                }
            }
        binding?.edConfirmPassword?.text.toString()
            .validateConfirmPassword(
                ValidatorInputTypesEnums.CONFIRM_PASSWORD,
                binding?.edNewPassword?.text.toString(),
                this
            ).let {
                if (!it.isValid) {
                    showErrorAlert(
                        title = resources.getString(R.string.confirm_password),
                        message = it.errorMessage
                    )
                    return false
                }
            }
        return valid
    }

    companion object {

        fun start(context: Context?) {
            val intent = Intent(context, ChangePasswordActivity::class.java)
            context?.startActivity(intent)
        }

    }

}