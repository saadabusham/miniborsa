package com.technzone.miniborsa.ui.auth.register.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.miniborsa.data.common.Constants
import com.technzone.miniborsa.data.common.CustomObserverResponse
import com.technzone.miniborsa.data.models.general.Countries
import com.technzone.miniborsa.databinding.FragmentRegisterBinding
import com.technzone.miniborsa.ui.auth.register.viewmodels.RegistrationViewModel
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
import com.technzone.miniborsa.ui.countrypicker.activity.CountriesPickerActivity
import com.technzone.miniborsa.utils.extensions.getDeviceCountryCode
import com.technzone.miniborsa.utils.extensions.readRawJson
import com.technzone.miniborsa.utils.extensions.showErrorAlert
import com.technzone.miniborsa.utils.extensions.validate
import com.technzone.miniborsa.utils.validation.ValidatorInputTypesEnums
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_toolbar.*

@AndroidEntryPoint
class RegistrationFragment :
        BaseBindingFragment<FragmentRegisterBinding>() {

    private val viewModel: RegistrationViewModel by activityViewModels()

    override fun getLayoutId(): Int = R.layout.fragment_register

    override fun onViewVisible() {
        super.onViewVisible()
        addToolbar(
                toolbarView = toolbar,
                hasToolbar = true,
                hasBackButton = true,
                hasTitle = false,
                showBackArrow = true
        )
        binding?.viewModel = viewModel
        setUpData()
        setUpListener()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    private fun setUpListener() {
        binding?.btnSignUp?.setOnClickListener {
            if (isDataValidate())
                viewModel.registerUser().observe(this, registerResultObserver())
        }
        binding?.tvSignIn?.setOnClickListener {
            requireActivity().finish()
        }
        requireContext().getDeviceCountryCode().let {
            viewModel.selectedCountryCode.postValue(it)
        }
        binding?.tvCountryCode?.setOnClickListener {
            CountriesPickerActivity.start(
                requireActivity(),
                selectedCountry = viewModel.selectedCountryCode.value,
                title = getString(R.string.done),
                actonTitle = getString(R.string.done),
                resultLauncher = resultLauncher
            )
        }
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                viewModel.selectedCountryCode.postValue(data?.getSerializableExtra(Constants.BundleData.COUNTRY) as Countries)
            }
        }

    private fun setUpData() {
        val localData: List<Countries> =
            readRawJson(requireContext(), R.raw.countries)
    }

    private fun registerResultObserver(): CustomObserverResponse<String> {
        return CustomObserverResponse(requireActivity(), object : CustomObserverResponse.APICallBack<String> {
            override fun onSuccess(statusCode: Int, subErrorCode: ResponseSubErrorsCodeEnum, data: String?) {
                viewModel.userIdMutableLiveData.value = data
                navigationController.navigate(R.id.action_registrationFragment_to_verificationSignUpFragment)
            }
        })
    }

    private fun isDataValidate(): Boolean {
        var valid = true
        binding?.edFirstName?.text.toString()
                .validate(ValidatorInputTypesEnums.FIRST_NAME, requireContext()).let {
                    if (!it.isValid) {
                        requireActivity().showErrorAlert(title = it.errorTitle,message = it.errorMessage)
                        return false
                    }
                }
        binding?.edLastName?.text.toString()
                .validate(ValidatorInputTypesEnums.LAST_NAME, requireContext()).let {
                    if (!it.isValid) {
                        requireActivity().showErrorAlert(title = it.errorTitle,message = it.errorMessage)
                        return false
                    }
                }
        binding?.edPhoneNumber?.text.toString()
                .validate(ValidatorInputTypesEnums.PHONE_NUMBER, requireContext()).let {
                    if (!it.isValid) {
                        requireActivity().showErrorAlert(title = it.errorTitle,message = it.errorMessage)
                        return false
                    }
                }
        binding?.edEmail?.text.toString()
                .validate(ValidatorInputTypesEnums.EMAIL, requireContext()).let {
                    if (!it.isValid) {
                        requireActivity().showErrorAlert(title = it.errorTitle,message = it.errorMessage)
                        return false
                    }
                }
        binding?.etPassword?.text.toString()
                .validate(ValidatorInputTypesEnums.PASSWORD, requireContext()).let {
                    if (!it.isValid) {
                        requireActivity().showErrorAlert(title = it.errorTitle,message = it.errorMessage)
                        return false
                    }
                }
        return valid
    }

}