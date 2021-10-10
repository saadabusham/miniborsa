package com.technzone.minibursa.ui.core.updateprofile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.technzone.minibursa.R
import com.technzone.minibursa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.minibursa.data.common.Constants
import com.technzone.minibursa.data.common.CustomObserverResponse
import com.technzone.minibursa.data.models.auth.login.UserDetailsResponseModel
import com.technzone.minibursa.data.models.general.Countries
import com.technzone.minibursa.databinding.ActivityUpdateProfileBinding
import com.technzone.minibursa.ui.base.activity.BaseBindingActivity
import com.technzone.minibursa.ui.base.dialogs.CompletedDialog
import com.technzone.minibursa.ui.core.changepassword.ChangePasswordActivity
import com.technzone.minibursa.ui.core.profile.viewmodels.ProfileViewModel
import com.technzone.minibursa.ui.countrypicker.activity.CountriesPickerActivity
import com.technzone.minibursa.utils.ImagePickerUtil.Companion.TAKE_USER_IMAGE_REQUEST_CODE
import com.technzone.minibursa.utils.extensions.getDeviceCountryCode
import com.technzone.minibursa.utils.extensions.showErrorAlert
import com.technzone.minibursa.utils.extensions.validate
import com.technzone.minibursa.utils.pickImages
import com.technzone.minibursa.utils.validation.ValidatorInputTypesEnums
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateProfileActivity : BaseBindingActivity<ActivityUpdateProfileBinding>() {

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile, hasToolbar = false)
        init()
        viewModel.getUser()
    }

    private fun init() {
        setUpBinding()
        setUpListeners()
        getDeviceCountryCode().let {
            viewModel.selectedCountryCode.postValue(it)
        }
        binding?.tvCountryCode?.setOnClickListener {
            CountriesPickerActivity.start(
                this,
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

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.imgBack?.setOnClickListener {
            onBackPressed()
        }
        binding?.cvUpdateImage?.setOnClickListener {
            pickImages(
                requestCode = TAKE_USER_IMAGE_REQUEST_CODE
            )
        }
        binding?.tvSave?.setOnClickListener {
            if (isDataValidate())
                viewModel.updateProfile().observe(this, updateProfileObserver())
        }
        binding?.cvPassword?.setOnClickListener {
            ChangePasswordActivity.start(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return

        when (requestCode) {
            TAKE_USER_IMAGE_REQUEST_CODE -> {
                val fileUri = data?.data
                fileUri?.path?.let {
                    viewModel.updateProfile(it).observe(this, updateProfileObserver())
                }
            }
        }
    }

    private fun updateProfileObserver(): CustomObserverResponse<Any> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<Any> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: Any?
                ) {
                    viewModel.getMyProfile().observe(this@UpdateProfileActivity, profileObserver())
                }
            }
        )
    }

    private fun profileObserver(): CustomObserverResponse<UserDetailsResponseModel> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<UserDetailsResponseModel> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: UserDetailsResponseModel?
                ) {
                    data?.let { it1 -> viewModel.storeUser(it1) }
                    viewModel.getUser()
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

    private fun isDataValidate(): Boolean {
        var valid = true
        binding?.edFirstName?.text.toString()
            .validate(ValidatorInputTypesEnums.FIRST_NAME, this).let {
                if (!it.isValid) {
                    showErrorAlert(title = it.errorTitle, message = it.errorMessage)
                    return false
                }
            }
        binding?.edLastName?.text.toString()
            .validate(ValidatorInputTypesEnums.LAST_NAME, this).let {
                if (!it.isValid) {
                    showErrorAlert(title = it.errorTitle, message = it.errorMessage)
                    return false
                }
            }
        binding?.edPhoneNumber?.text.toString()
            .validate(ValidatorInputTypesEnums.PHONE_NUMBER, this).let {
                if (!it.isValid) {
                    showErrorAlert(title = it.errorTitle, message = it.errorMessage)
                    return false
                }
            }
        return valid
    }

    companion object {

        fun start(
            context: Context?
        ) {
            val intent = Intent(context, UpdateProfileActivity::class.java)
            context?.startActivity(intent)
        }
    }
}