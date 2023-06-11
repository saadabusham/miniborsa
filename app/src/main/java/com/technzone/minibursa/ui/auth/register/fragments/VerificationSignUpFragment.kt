package com.technzone.minibursa.ui.auth.register.fragments

import androidx.fragment.app.activityViewModels
import com.technzone.minibursa.R
import com.technzone.minibursa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.minibursa.data.common.CustomObserverResponse
import com.technzone.minibursa.data.models.auth.login.UserDetailsResponseModel
import com.technzone.minibursa.databinding.FragmentVerificationRegisterBinding
import com.technzone.minibursa.ui.auth.register.viewmodels.RegistrationViewModel
import com.technzone.minibursa.ui.base.fragment.BaseBindingFragment
import com.technzone.minibursa.ui.userrole.activity.UserRolesActivity
import com.technzone.minibursa.utils.extensions.showErrorAlert
import com.technzone.minibursa.utils.extensions.validate
import com.technzone.minibursa.utils.validation.ValidatorInputTypesEnums
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerificationSignUpFragment : BaseBindingFragment<FragmentVerificationRegisterBinding>() {

    private val viewModel: RegistrationViewModel by activityViewModels()

    override fun getLayoutId(): Int = R.layout.fragment_verification_register

    override fun onViewVisible() {
        super.onViewVisible()
        addToolbar(
            hasToolbar = true,
            toolbarView =  binding?.layoutToolbar?.toolbar,
            tvToolbarTitleView = binding?.layoutToolbar?.tvToolbarTitle,
            hasBackButton = true,
            showBackArrow = true,
            hasTitle = true,
            title = R.string.empty_string
        )
        setUpViewsListeners()
        setUpData()
    }

    private fun setUpData() {
        binding?.viewModel = viewModel
        viewModel.startHandleResendSignUpPinCodeTimer()
    }

    private fun verifyOtpResultObserver(): CustomObserverResponse<UserDetailsResponseModel> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<UserDetailsResponseModel> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: UserDetailsResponseModel?
                ) {
                    data?.let {
                        viewModel.storeUser(it)
//                    navigationController.navigate(R.id.action_verificationSignUpFragment_to_registerSuccessFragment)
                        UserRolesActivity.start(requireContext())
                    }
                }
            })
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
                    viewModel.startHandleResendSignUpPinCodeTimer()
                }
            })
    }

    private fun setUpViewsListeners() {
        binding?.otpView?.setAnimationEnable(true)
        binding?.tvTimeToResend?.setOnClickListener {
            if (viewModel.signUpResendPinCodeEnabled.value == true) {
                viewModel.resendVerificationCode().observe(this, sendOtpResultObserver())
            }
        }
        binding?.btnConfirm?.setOnClickListener {
            if (validateInput()) {
                viewModel.verifyCode().observe(this, verifyOtpResultObserver())
            }
        }
    }

    private fun validateInput(): Boolean {
        binding?.otpView?.text.toString().validate(ValidatorInputTypesEnums.OTP, requireContext()).let {
            if (!it.isValid) {
                activity.showErrorAlert(it.errorTitle, it.errorMessage)
                return false
            }
        }
        return true
    }

}