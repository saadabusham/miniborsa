package com.technzone.minibursa.ui.core.profile.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.technzone.minibursa.common.CommonEnums
import com.technzone.minibursa.data.api.response.APIResource
import com.technzone.minibursa.data.enums.UserEnums
import com.technzone.minibursa.data.enums.UserRoleEnums
import com.technzone.minibursa.data.models.auth.login.UserDetailsResponseModel
import com.technzone.minibursa.data.models.general.Countries
import com.technzone.minibursa.data.pref.configuration.ConfigurationPref
import com.technzone.minibursa.data.pref.user.UserPref
import com.technzone.minibursa.data.repos.searchbusiness.SearchedBusinessRepo
import com.technzone.minibursa.data.repos.user.UserRepo
import com.technzone.minibursa.ui.base.viewmodel.BaseViewModel
import com.technzone.minibursa.utils.LocaleUtil
import com.technzone.minibursa.utils.extensions.checkPhoneNumberFormat
import com.technzone.minibursa.utils.extensions.createImageMultipart
import com.technzone.minibursa.utils.extensions.getRequestBody
import com.technzone.minibursa.utils.pref.SharedPreferencesUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val userPref: UserPref,
    private val sharedPreferencesUtil: SharedPreferencesUtil,
    private val configurationPref: ConfigurationPref,
    private val searchedBusinessRepo: SearchedBusinessRepo
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

    fun logout() = viewModelScope.launch {
        sharedPreferencesUtil.clearPreference()
        searchedBusinessRepo.clearBusinesses()
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
        val response = userRepo.refreshToken(userRepo.getUser()?.refreshToken?.refreshToken?:"")
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