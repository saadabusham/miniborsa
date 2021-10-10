package com.technzone.minibursa.ui.splash

import androidx.lifecycle.liveData
import com.technzone.minibursa.data.api.response.APIResource
import com.technzone.minibursa.data.enums.UserEnums
import com.technzone.minibursa.data.models.auth.login.UserDetailsResponseModel
import com.technzone.minibursa.data.pref.favorite.FavoritePref
import com.technzone.minibursa.data.repos.common.CommonRepo
import com.technzone.minibursa.data.repos.configuration.ConfigurationRepo
import com.technzone.minibursa.data.repos.user.UserRepo
import com.technzone.minibursa.ui.base.viewmodel.BaseViewModel
import com.technzone.minibursa.utils.pref.SharedPreferencesUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val configurationRepo: ConfigurationRepo,
    private val sharedPreferencesUtil: SharedPreferencesUtil,
    private var favoritePref: FavoritePref,
    private val commonRepo: CommonRepo
) : BaseViewModel() {

    fun getConfigurationData() = liveData {
        emit(APIResource.loading())
        val response = configurationRepo.loadConfigurationData()
        emit(response)
    }

//    fun updateAccessToken() {
//        updateTokenResult.postValue(Result.Loading)
//        compositeDisposable + userRepo.updateAccessToken(getAccessToken())
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                if (it.success) {
//                    storeUser(it.data!!)
//                    it.data?.token?.let { it1 -> userRepo.saveAccessToken(it1) }
//                    updateTokenResult.postValue(ResponseSubErrorsCodeEnum.Success(it))
//                } else {
//                    updateTokenResult.postValue(
//                        Result.CustomError(
//                            errorCode = it.code,
//                            message = it.message
//                        )
//                    )
//                }
//            }, {
//                updateTokenResult.postValue(Result.Error(it))
//            })
//
//    }
//    fun storeUser(user: UserDetailsResponseModel) {
//        user.id?.let {
//            userRepo.setUserId(it)
//        }
//        user.token?.let { userRepo.saveAccessToken(it) }
//        userRepo.setUserStatus(UserEnums.UserState.LoggedIn)
//        userRepo.setUser(user)
//    }
//
//    fun getAccessToken(): String {
//        return userRepo.getUser()?.refreshToken?.refreshToken ?: ""
//    }
//
//
//    fun logout() {
//        if (userRepo.getTouchIdStatus())
//            sharedPreferencesUtil.logout()
//        else
//            sharedPreferencesUtil.clearPreference()
//    }

    fun isUserLoggedIn() = userRepo.getUserStatus() == UserEnums.UserState.LoggedIn

    fun getUser(): UserDetailsResponseModel? {
        return userRepo.getUser()
    }

    fun getCurrentUserRoles(): String {
        return userRepo.getCurrentRole()
    }
}