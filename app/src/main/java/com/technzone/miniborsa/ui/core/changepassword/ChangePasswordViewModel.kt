package com.technzone.miniborsa.ui.core.changepassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.liveData
import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.miniborsa.data.api.response.ResponseWrapper
import com.technzone.miniborsa.data.pref.configuration.ConfigurationPref
import com.technzone.miniborsa.data.repos.user.UserRepo
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import com.technzone.miniborsa.utils.pref.SharedPreferencesUtil
import dagger.assisted.Assisted
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