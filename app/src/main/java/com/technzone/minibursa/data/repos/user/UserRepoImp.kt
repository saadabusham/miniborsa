package com.technzone.minibursa.data.repos.user

import com.technzone.minibursa.data.api.response.APIResource
import com.technzone.minibursa.data.api.response.ResponseHandler
import com.technzone.minibursa.data.api.response.ResponseWrapper
import com.technzone.minibursa.data.daos.remote.user.UserRemoteDao
import com.technzone.minibursa.data.enums.UserEnums
import com.technzone.minibursa.data.models.auth.login.UserDetailsResponseModel
import com.technzone.minibursa.data.pref.user.UserPref
import com.technzone.minibursa.data.repos.base.BaseRepo
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class UserRepoImp @Inject constructor(
    responseHandler: ResponseHandler,
    private val userRemoteDao: UserRemoteDao,
    private val userPref: UserPref
) : BaseRepo(responseHandler), UserRepo {


    override suspend fun login(
        userName: String,
        password: String
    ): APIResource<ResponseWrapper<UserDetailsResponseModel>> {
        return try {
            responseHandle.handleSuccess(
                userRemoteDao.login(
                    userName,
                    password
                )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun register(
        password: String,
        firstName: String,
        lastName: String,
        phoneNumber: String,
        email: String,
        registrationId: String,
        deviceType: Int,
        applicationType: Int
    ): APIResource<ResponseWrapper<String>> {
        return try {
            responseHandle.handleSuccess(
                userRemoteDao.register(
                    password,
                    firstName,
                    lastName,
                    phoneNumber,
                    email,
                    registrationId,
                    deviceType,
                    applicationType
                )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun forgetPassword(email: String): APIResource<ResponseWrapper<String>> {
        return try {
            responseHandle.handleSuccess(
                userRemoteDao.forgetPassword(
                    email
                )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun verify(
        userId: String,
        verificationCode: String
    ): APIResource<ResponseWrapper<UserDetailsResponseModel>> {
        return try {
            responseHandle.handleSuccess(
                userRemoteDao.verify(
                    userId, verificationCode
                )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun resendCode(phoneNumber: String): APIResource<ResponseWrapper<String>> {
        return try {
            responseHandle.handleSuccess(
                userRemoteDao.resendCode(
                    phoneNumber
                )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun refreshToken(refreshToken: String): APIResource<ResponseWrapper<UserDetailsResponseModel>> {
        return try {
            responseHandle.handleSuccess(
                userRemoteDao.refreshToken(
                    refreshToken
                )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getProfile(): APIResource<ResponseWrapper<UserDetailsResponseModel>> {
        return try {
            responseHandle.handleSuccess(userRemoteDao.getProfile())
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun updateProfile(
        email: RequestBody?,
        firstName: RequestBody,
        lastName: RequestBody,
        phoneNumber: RequestBody
    ): APIResource<ResponseWrapper<Any>> {
        return try {
            responseHandle.handleSuccess(
                userRemoteDao.updateProfile(
                    email, firstName, lastName, phoneNumber
                )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun updatePassword(
        oldPassword: String,
        newPassword: String
    ): APIResource<ResponseWrapper<Any>> {
        return try {
            responseHandle.handleSuccess(
                userRemoteDao.updatePassword(
                    oldPassword, newPassword
                )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun resetPassword(
        newPassword: String
    ): APIResource<ResponseWrapper<Any>> {
        return try {
            responseHandle.handleSuccess(
                userRemoteDao.resetPassword(
                    newPassword
                )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun updateFcmToken(
        registrationId: RequestBody,
        deviceType: RequestBody
    ): APIResource<ResponseWrapper<Any>> {
        return try {
            responseHandle.handleSuccess(
                userRemoteDao.updateFcmToken(
                    registrationId, deviceType
                )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun updateProfilePicture(image: MultipartBody.Part): APIResource<ResponseWrapper<Any>> {
        return try {
            responseHandle.handleSuccess(
                userRemoteDao.updateProfilePicture(
                    image
                )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override fun saveAccessToken(accessToken: String) {
        userPref.saveAccessToken(accessToken)
    }

    override fun getAccessToken(): String {
        return userPref.getAccessToken()
    }

    override fun saveUserPassword(password: String) {
        userPref.saveUserPassword(password)
    }

    override fun getUserPassword(): String {
        return userPref.getUserPassword()
    }

    override fun setUserStatus(userState: UserEnums.UserState) {
        userPref.setUserStatus(userState.ordinal)
    }

    override fun getUserStatus(): UserEnums.UserState {
        return UserEnums.UserState.getUserStateByPosition(userPref.getUserStatus())
    }


    override fun saveNotificationStatus(flag: Boolean) {
        userPref.setNotificationStatus(flag)
    }

    override fun getNotificationStatus(): Boolean {
        return userPref.getNotificationStatus()
    }

    override fun saveTouchIdStatus(flag: Boolean) {
        userPref.setTouchIdStatus(flag)
    }

    override fun getTouchIdStatus(): Boolean {
        return userPref.getTouchIdStatus()
    }

    override fun setUser(user: UserDetailsResponseModel) {
        userPref.setUser(user)
    }

    override fun getUser(): UserDetailsResponseModel? {
        return userPref.getUser()
    }

    override fun setCurrentRole(role: String) {
        userPref.setCurrentRole(role)
    }

    override fun getCurrentRole(): String {
        return userPref.getCurrentRole()
    }

    override fun setIsFirstLogin(isFirstLogin: Boolean) {
        userPref.setIsFirstLogin(isFirstLogin)
    }

    override fun getIsFirstLogin(): Boolean {
        return userPref.getIsFirstLogin()
    }

}