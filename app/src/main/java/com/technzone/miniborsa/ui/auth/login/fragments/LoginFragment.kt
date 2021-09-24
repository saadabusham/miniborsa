package com.technzone.miniborsa.ui.auth.login.fragments

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.activityViewModels
import co.infinum.goldfinger.Goldfinger
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.miniborsa.data.common.Constants
import com.technzone.miniborsa.data.common.CustomObserverResponse
import com.technzone.miniborsa.data.enums.UserRoleEnums
import com.technzone.miniborsa.data.models.auth.login.UserDetailsResponseModel
import com.technzone.miniborsa.data.pref.user.UserPref
import com.technzone.miniborsa.databinding.FragmentLoginBinding
import com.technzone.miniborsa.ui.auth.forgetpassword.ForgetPasswordActivity
import com.technzone.miniborsa.ui.auth.login.viewmodels.LoginViewModel
import com.technzone.miniborsa.ui.auth.register.RegisterActivity
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
import com.technzone.miniborsa.ui.investor.invistormain.activity.InvestorMainActivity
import com.technzone.miniborsa.ui.userrole.activity.UserRolesActivity
import com.technzone.miniborsa.utils.extensions.*
import com.technzone.miniborsa.utils.validation.ValidatorInputTypesEnums
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.layout_toolbar_with_skip.*
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
            toolbarView = toolbar,
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
        tvSignUp?.setOnClickListener {
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
        tvForgetPassword?.setOnClickListener {
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
                                viewModel.storeUser(it)
                                if (requireActivity().intent.getBooleanExtra(
                                        Constants.BundleData.IS_ACTIVITY_RESULT,
                                        false
                                    )
                                ) {
                                    requireActivity().setResult(Activity.RESULT_OK, Intent().apply {
                                        this.putExtra(Constants.BundleData.IS_LOGIN_SUCCESS, true)
                                    })
                                    requireActivity().finish()
                                } else {
                                    goToNextScreen(it)
                                }
                            }
                        }
                    }
                }
            })
    }

    private fun goToNextScreen(data: UserDetailsResponseModel) {
        if (data.roles.isNullOrEmpty() || (data.roles[0].role == UserRoleEnums.VISITOR_ROLE.value && data.roles.size == 1))
            UserRolesActivity.start(requireContext())
        else {
            InvestorMainActivity.start(requireContext())
            viewModel.setUserRole(UserRoleEnums.INVESTOR_ROLE.value)
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
        edEmail.text.toString().validate(
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