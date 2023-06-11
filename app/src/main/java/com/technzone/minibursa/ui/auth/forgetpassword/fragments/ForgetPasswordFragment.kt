package com.technzone.minibursa.ui.auth.forgetpassword.fragments

import androidx.fragment.app.activityViewModels
import com.technzone.minibursa.R
import com.technzone.minibursa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.minibursa.data.common.CustomObserverResponse
import com.technzone.minibursa.databinding.FragmentForgetPasswordBinding
import com.technzone.minibursa.ui.auth.forgetpassword.viewmodels.ForgetPasswordViewModel
import com.technzone.minibursa.ui.base.fragment.BaseBindingFragment
import com.technzone.minibursa.utils.extensions.showErrorAlert
import com.technzone.minibursa.utils.extensions.validate
import com.technzone.minibursa.utils.validation.ValidatorInputTypesEnums
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgetPasswordFragment : BaseBindingFragment<FragmentForgetPasswordBinding>() {

    private val viewModel: ForgetPasswordViewModel by activityViewModels()

    override fun getLayoutId(): Int = R.layout.fragment_forget_password

    override fun onViewVisible() {
        super.onViewVisible()
        addToolbar(
            hasToolbar = true,
            toolbarView =  binding?.layoutToolbar?.toolbar,
            tvToolbarTitleView = binding?.layoutToolbar?.tvToolbarTitle,
            hasBackButton = true,
            showBackArrow = true,
            hasTitle = false,
            title = R.string.empty_string
        )
        setUpBinding()
        setUpListeners()
    }


    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.btnSendCode?.setOnClickListener {
            if (validateInput()) {
                viewModel.resendVerificationCode().observe(this, sendOtpResultObserver())
            }
        }
    }

    private fun sendOtpResultObserver(): CustomObserverResponse<String> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<String> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: String?
                ) {
                    viewModel.userIdMutableLiveData.value = data
                    navigationController.navigate(R.id.action_forgetPassword_to_verificationForgetPassword)
                }
            })
    }

    private fun validateInput(): Boolean {
        binding?.edEmail?.text.toString().validate(ValidatorInputTypesEnums.EMAIL, requireContext())
            .let {
                if (!it.isValid) {
                    requireActivity().showErrorAlert(
                        title = it.errorTitle,
                        message = it.errorMessage
                    )
                    return false
                }
            }
        return true
    }


}