package com.technzone.miniborsa.ui.core.profile.viewmodels

import androidx.lifecycle.liveData
import com.technzone.miniborsa.common.CommonEnums
import com.technzone.miniborsa.data.pref.configuration.ConfigurationPref
import com.technzone.miniborsa.data.pref.user.UserPref
import com.technzone.miniborsa.data.repos.user.UserRepo
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import com.technzone.miniborsa.utils.LocaleUtil
import com.technzone.miniborsa.utils.pref.SharedPreferencesUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val userPref: UserPref,
    private val sharedPreferencesUtil: SharedPreferencesUtil,
    private val configurationPref: ConfigurationPref
) : BaseViewModel() {

    fun getCurrentUserRoles(): String {
        return userRepo.getCurrentRole()
    }
    fun setCurrentUserRoles(role:String) {
        return userRepo.setCurrentRole(role)
    }

    fun logout() {
//        sharedPreferencesUtil.logout()
        sharedPreferencesUtil.clearPreference()
        userPref.setIsFirstOpen(false)
        configurationPref.setAppLanguageValue(LocaleUtil.getLanguage())
    }

    fun saveLanguage() = liveData {
        configurationPref.setAppLanguageValue(
            if (LocaleUtil.getLanguage() == CommonEnums.Languages.English.value)
                CommonEnums.Languages.Arabic.value
            else CommonEnums.Languages.English.value
        )
        emit(null)
    }
}