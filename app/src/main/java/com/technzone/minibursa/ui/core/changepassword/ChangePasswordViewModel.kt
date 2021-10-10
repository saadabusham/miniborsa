package com.technzone.minibursa.ui.core.changepassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.technzone.minibursa.data.api.response.APIResource
import com.technzone.minibursa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.minibursa.data.api.response.ResponseWrapper
import com.technzone.minibursa.data.pref.configuration.ConfigurationPref
import com.technzone.minibursa.data.repos.user.UserRepo
import com.technzone.minibursa.ui.base.viewmodel.BaseViewModel
import com.technzone.minibursa.utils.pref.SharedPreferencesUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    val sharedPreferencesUtil: SharedPreferencesUtil,
    val configurationPref: ConfigurationPref,
    val userRepo: UserRepo
) : BaseViewModel() {

    val oldPasswordMutableLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val newPasswordMutableLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val confirmPasswordMutableLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }


    val changePasswordResult: MutableLiveData<Result<ResponseWrapper<Any>>> = MutableLiveData()

    fun changePassword() = liveData {
        emit(APIResource.loading())
        val response = userRepo.updatePassword(
                oldPassword = oldPasswordMutableLiveData.value.toString(),
                newPassword = newPasswordMutableLiveData.value.toString()
        )
        if(response.statusSubCode == ResponseSubErrorsCodeEnum.Success)
            userRepo.saveUserPassword(newPasswordMutableLiveData.value?:"")
        emit(response)
    }

}