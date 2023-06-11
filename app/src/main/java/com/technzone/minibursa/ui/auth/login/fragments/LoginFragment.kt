package com.technzone.minibursa.ui.auth.login.fragments

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.activityViewModels
import co.infinum.goldfinger.Goldfinger
import com.technzone.minibursa.R
import com.technzone.minibursa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.minibursa.data.common.Constants
import com.technzone.minibursa.data.common.CustomObserverResponse
import com.technzone.minibursa.data.enums.UserRoleEnums
import com.technzone.minibursa.data.models.auth.login.UserDetailsResponseModel
import com.technzone.minibursa.data.pref.user.UserPref
import com.technzone.minibursa.databinding.FragmentLoginBinding
import com.technzone.minibursa.ui.auth.forgetpassword.ForgetPasswordActivity
import com.technzone.minibursa.ui.auth.login.viewmodels.LoginViewModel
import com.technzone.minibursa.ui.auth.register.RegisterActivity
import com.technzone.minibursa.ui.base.fragment.BaseBindingFragment
import com.technzone.minibursa.ui.business.businessmain.activity.BusinessMainActivity
import com.technzone.minibursa.ui.investor.invistormain.activity.InvestorMainActivity
import com.technzone.minibursa.ui.userrole.activity.UserRolesActivity
import com.technzone.minibursa.utils.extensions.*
import com.technzone.minibursa.utils.validation.ValidatorInputTypesEnums
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LoginFragment : BaseBindingFragment<FragmentLoginBinding>() {

    @Inject
    lateinit var prefs: UserPref
    private val viewModel: LoginViewModel by activityViewModels()

    override fun getLayoutId(): Int = R.layout.fragment_login

    override fun onViewVisible() {
        super.onViewVisible()
        prefs.setIsFirstOpen(false)
        addToolbar(
            toolbarView =  binding?.layoutToolbar?.toolbar,
            tvToolbarTitleView = binding?.layoutToolbar?.tvToolbarTitle,
            hasToolbar = true,
            hasBackButton = requireActivity().intent.getBooleanExtra(
                Constants.BundleData.IS_ACTIVITY_RESULT,
                false
            ),
            hasTitle = false,
            showBackArrow = requireActivity().intent.getBooleanExtra(
                Constants.BundleData.IS_ACTIVITY_RESULT,
                false
            )
        )
        setUpBinding()
        setUpListeners()
        setUpData()
        checkIfThereUpdate()
    }

    private fun setUpData() {

    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.btnlogin?.setOnClickListener {
            if (validateInput())
                viewModel.loginUser(
                    viewModel.email.value.toString(),
                    viewModel.passwordMutableLiveData.value.toString()
                ).observe(this, loginResultObserver())
        }
        binding?.layoutToolbar?.tvAction?.setOnClickListener {
            if (requireActivity().intent.getBooleanExtra(
                    Constants.BundleData.IS_ACTIVITY_RESULT,
                    false
                )
            ) {
                requireActivity().finish()
            } else {
                InvestorMainActivity.start(requireContext())
            }
        }
        binding?.tvSignUp?.setOnClickListener {
            if (requireActivity().intent.getBooleanExtra(
                    Constants.BundleData.IS_ACTIVITY_RESULT,
                    false
                )
            ) {
                RegisterActivity.startForResult(requireActivity(), true)
            } else {
                RegisterActivity.startForResult(requireActivity(), false)
            }
        }
        binding?.tvForgetPassword?.setOnClickListener {
            ForgetPasswordActivity.start(context)
        }

        binding?.imgIdRecognition?.setOnClickListener {
            autoLogin()
        }
    }

    private fun loginResultObserver(): CustomObserverResponse<UserDetailsResponseModel> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<UserDetailsResponseModel> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: UserDetailsResponseModel?
                ) {
                    when (statusCode) {
                        ResponseSubErrorsCodeEnum.EmailNotVerified.value -> {
                            navigationController.navigate(
                                R.id.action_loginFragment_to_verificationLoginFragment
                            )
                        }
                        ResponseSubErrorsCodeEnum.Success.value -> {
                            data?.let {
                                handleSuccessLogin(it)
                            }
                        }
                    }
                }
            })
    }

    private fun handleSuccessLogin(data: UserDetailsResponseModel){
        viewModel.storeUser(data)
        if (requireActivity().intent.getBooleanExtra(
                Constants.BundleData.IS_ACTIVITY_RESULT,
                false
            )
        ) {
            data.roles?.singleOrNull { it.role == UserRoleEnums.INVESTOR_ROLE.value }
                ?.let {
                    viewModel.setUserRole(UserRoleEnums.INVESTOR_ROLE.value)
                }
            requireActivity().setResult(Activity.RESULT_OK, Intent().apply {
                this.putExtra(Constants.BundleData.IS_LOGIN_SUCCESS, true)
            })
            requireActivity().finish()
        } else {
            goToNextScreen(data)
        }
    }

    private fun goToNextScreen(data: UserDetailsResponseModel) {
        if (data.roles.isNullOrEmpty() || (data.roles[0].role == UserRoleEnums.VISITOR_ROLE.value && data.roles.size == 1))
            UserRolesActivity.start(requireContext())
        else if (data.roles.singleOrNull { it.role == UserRoleEnums.INVESTOR_ROLE.value} !=null){
            viewModel.setUserRole(UserRoleEnums.INVESTOR_ROLE.value)
            InvestorMainActivity.start(requireContext())
        } else if (data.roles.singleOrNull { it.role == UserRoleEnums.BUSINESS_ROLE.value} !=null){
            viewModel.setUserRole(UserRoleEnums.BUSINESS_ROLE.value)
            BusinessMainActivity.start(requireContext())
        }
    }

    private fun checkIfThereUpdate() {
//        if (viewModel.checkIfThereUpdate()) {
//            requireActivity().showUpdateDialog(viewModel.getUpdateStatus())
//            if (!viewModel.checkIsUpdateMandatory()) {
//                setUpTouchId()
//            }
//        } else {
//            setUpTouchId()
//        }
        setUpTouchId()
    }

    private fun setUpTouchId() {
        val goldFinger = Goldfinger.Builder(requireContext()).build()
        if (viewModel.getTouchIdStatus()) {
            autoLogin()
            if (goldFinger.canAuthenticate()) {
                binding?.imgIdRecognition?.visible()
                binding?.linearSignInOption?.visible()
            }
        }

    }

    private fun autoLogin() {
        activity?.let {
            val goldFinger = Goldfinger.Builder(it).build()

            if (goldFinger.canAuthenticate()) {
                val params = Goldfinger.PromptParams.Builder(it)
                    .title(getString(R.string.login))
                    .negativeButtonText(getString(R.string.cancel))
                    .build()

                goldFinger.authenticate(params, object : Goldfinger.Callback {
                    override fun onError(e: Exception) {
                        /* Critical error happened */
                    }

                    override fun onResult(result: Goldfinger.Result) {
                        if (result.type() == Goldfinger.Type.SUCCESS) {
                            viewModel.loginUser(
                                viewModel.loadPhoneNumberLocal(),
                                viewModel.loadPasswordLocal()
                            ).observe(this@LoginFragment, loginResultObserver())
                        }
                    }
                })
            }
        }
    }

    private fun validateInput(): Boolean {
        var valid = true
        binding?.edEmail?.text.toString().validate(
            ValidatorInputTypesEnums.EMAIL,
            requireContext()
        )
            .let {
                if (!it.isValid) {
                    requireActivity().showErrorAlert(
                        title = it.errorTitle,
                        message = it.errorMessage
                    )
                    return false
                }
            }
        binding?.edPassword?.text.toString().validate(
            ValidatorInputTypesEnums.PASSWORD,
            requireContext()
        )
            .let {
                if (!it.isValid) {
                    requireActivity().showErrorAlert(
                        title = it.errorTitle,
                        message = it.errorMessage
                    )
                    return false
                }
            }
        return valid
    }

}