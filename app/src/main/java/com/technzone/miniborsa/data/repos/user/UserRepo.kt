package com.technzone.miniborsa.data.repos.user

import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.api.response.ResponseWrapper
import com.technzone.miniborsa.data.enums.UserEnums
import com.technzone.miniborsa.data.models.auth.login.UserDetailsResponseModel
import okhttp3.MultipartBody
import okhttp3.RequestBody


interface UserRepo {


    suspend fun login(
        userName: String,
        password: String
    ): APIResource<ResponseWrapper<UserDetailsResponseModel>>

    suspend fun register(
        password: String,
        firstName: String,
        lastName: String,
        phoneNumber: String,
        email: String,
        registrationId: String,
        deviceType: Int,
        applicationType: Int
    ): APIResource<ResponseWrapper<String>>

    suspend fun forgetPassword(
        email: String
    ): APIResource<ResponseWrapper<String>>

    suspend fun verify(
        userId: String,
        verificationCode: String
    ): APIResource<ResponseWrapper<UserDetailsResponseModel>>

    suspend fun resendCode(
        phoneNumber: String
    ): APIResource<ResponseWrapper<String>>

    suspend fun refreshToken(
        refreshToken: String
    ): APIResource<ResponseWrapper<UserDetailsResponseModel>>

    suspend fun getProfile(
    ): APIResource<ResponseWrapper<UserDetailsResponseModel>>

    suspend fun updateProfile(
        email: RequestBody?,
        firstName: RequestBody,
        lastName: RequestBody,
        gender: RequestBody,
        dateOfBirth: RequestBody?
    ): APIResource<ResponseWrapper<UserDetailsResponseModel>>

    suspend fun updatePassword(
        oldPassword: String,
        newPassword: String
    ): APIResource<ResponseWrapper<Any>>

    suspend fun resetPassword(
        newPassword: String
    ): APIResource<ResponseWrapper<Any>>

    suspend fun updateFcmToken(
        registrationId: RequestBody,
        deviceType: RequestBody
    ): APIResource<ResponseWrapper<Any>>

    suspend fun updateProfilePicture(
        image: MultipartBody.Part
    ): APIResource<ResponseWrapper<Any>>

    suspend fun registerInvestor(
        jobTitle: String,
        investmentBudget: Double,
        investmentBudgetNA: Boolean,
        countries: List<Int>,
        categories: List<Int>
    ): APIResource<ResponseWrapper<Any>>

    fun saveNotificationStatus(flag: Boolean)
    fun getNotificationStatus(): Boolean

    fun saveTouchIdStatus(flag: Boolean)
    fun getTouchIdStatus(): Boolean

    fun saveAccessToken(accessToken: String)
    fun getAccessToken(): String

    fun saveUserPassword(password: String)
    fun getUserPassword(): String

    fun setUserStatus(userState: UserEnums.UserState)
    fun getUserStatus(): UserEnums.UserState

    fun setUser(user: UserDetailsResponseModel)
    fun getUser(): UserDetailsResponseModel?

    fun setCurrentRole(role: String)
    fun getCurrentRole(): String

}