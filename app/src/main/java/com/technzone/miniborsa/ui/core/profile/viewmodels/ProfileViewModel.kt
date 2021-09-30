package com.technzone.miniborsa.ui.core.profile.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.technzone.miniborsa.common.CommonEnums
import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.enums.UserEnums
import com.technzone.miniborsa.data.enums.UserRoleEnums
import com.technzone.miniborsa.data.models.auth.login.UserDetailsResponseModel
import com.technzone.miniborsa.data.models.general.Countries
import com.technzone.miniborsa.data.pref.configuration.ConfigurationPref
import com.technzone.miniborsa.data.pref.user.UserPref
import com.technzone.miniborsa.data.repos.business.BusinessRepo
import com.technzone.miniborsa.data.repos.user.UserRepo
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import com.technzone.miniborsa.utils.LocaleUtil
import com.technzone.miniborsa.utils.extensions.checkPhoneNumberFormat
import com.technzone.miniborsa.utils.extensions.createImageMultipart
import com.technzone.miniborsa.utils.extensions.getRequestBody
import com.technzone.miniborsa.utils.pref.SharedPreferencesUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val userPref: UserPref,
    private val sharedPreferencesUtil: SharedPreferencesUtil,
    private val configurationPref: ConfigurationPref,
    private val businessRepo: BusinessRepo
) : BaseViewModel() {

    val phoneNumberWithoutCountryCode: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val selectedCountryCode: MutableLiveData<Countries> by lazy { MutableLiveData<Countries>() }
    val firstNameMutableLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val lastNameMutableLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val emailMutableLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val userImageMutableLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun getCurrentUserRoles(): String {
        return userRepo.getCurrentRole()
    }

    fun isUserHasBusinessRoles(): Boolean {
        return userRepo.getUser()?.roles?.singleOrNull { it.role == UserRoleEnums.BUSINESS_ROLE.value } != null
    }

    fun isUserHasInvestorRoles(): Boolean {
        return userRepo.getUser()?.roles?.singleOrNull { it.role == UserRoleEnums.INVESTOR_ROLE.value } != null
    }

    fun setCurrentUserRoles(role: String) {
        return userRepo.setCurrentRole(role)
    }

    fun logout() {
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

    fun updateProfile(path: String) = liveData {
        emit(APIResource.loading())
        val response = userRepo.updateProfilePicture(path.createImageMultipart("Image"))
        emit(response)
    }

    fun getMyProfile() = liveData {
        emit(APIResource.loading())
        val response = userRepo.getProfile()
        emit(response)
    }

    fun storeUser(user: UserDetailsResponseModel) {
        userRepo.setUser(user)
    }

    fun isUserLoggedIn() = userRepo.getUserStatus() == UserEnums.UserState.LoggedIn

    fun getUser() {
        userRepo.getUser()?.let {
            firstNameMutableLiveData.value = it.firstName
            lastNameMutableLiveData.value = it.lastName
            emailMutableLiveData.value = it.email
            phoneNumberWithoutCountryCode.value = it.phoneNumber?.checkPhoneNumberFormat()
            userImageMutableLiveData.value = it.picture
        }
    }

    fun updateProfile() = liveData {
        emit(APIResource.loading())
        val response = userRepo.updateProfile(
            firstName = firstNameMutableLiveData.value.toString().getRequestBody(),
            lastName = lastNameMutableLiveData.value.toString().getRequestBody(),
            phoneNumber = (selectedCountryCode.value?.code + phoneNumberWithoutCountryCode.value.toString()
                .checkPhoneNumberFormat()).getRequestBody(),
        )
        emit(response)
    }
}