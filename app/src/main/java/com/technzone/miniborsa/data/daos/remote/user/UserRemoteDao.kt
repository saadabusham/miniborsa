package com.technzone.miniborsa.data.daos.remote.user

import com.technzone.miniborsa.data.api.response.ResponseWrapper
import com.technzone.miniborsa.data.common.NetworkConstants
import com.technzone.miniborsa.data.models.auth.login.UserDetailsResponseModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface UserRemoteDao {

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:true")
    @FormUrlEncoded
    @POST("api/user/authenticate")
    suspend fun login(
        @Field("Username") userName: String,
        @Field("Password") password: String
    ): ResponseWrapper<UserDetailsResponseModel>


    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:true")
    @FormUrlEncoded
    @POST("api/User/register")
    suspend fun register(
        @Field("Password") password: String,
        @Field("FullName") fullName: String,
        @Field("Email") email: String
    ): ResponseWrapper<String>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:true")
    @FormUrlEncoded
    @POST("api/User/verify")
    suspend fun verify(
        @Field("UserId") userId: String,
        @Field("VerificationCode") verificationCode: String
    ): ResponseWrapper<UserDetailsResponseModel>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:true")
    @GET("api/User/resendCode")
    suspend fun resendCode(
        @Query("PhoneNumber") phoneNumber: String
    ): ResponseWrapper<String>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @FormUrlEncoded
    @POST("api/User/refreshToken")
    suspend fun refreshToken(
        @Field("RefreshToken") refreshToken: String
    ): ResponseWrapper<UserDetailsResponseModel>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("api/User/profile")
    suspend fun getProfile(
    ): ResponseWrapper<UserDetailsResponseModel>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @Multipart
    @PATCH("api/User/update")
    suspend fun updateProfile(
        @Part("Email") email: RequestBody?,
        @Part("FirstName") firstName: RequestBody,
        @Part("LastName") lastName: RequestBody,
        @Part("Gender") gender: RequestBody,
        @Part("Birthdate") dateOfBirth: RequestBody?
    ): ResponseWrapper<UserDetailsResponseModel>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @FormUrlEncoded
    @POST("api/User/ChangePassword")
    suspend fun updatePassword(
        @Field("OldPassword") oldPassword: String,
        @Field("NewPassword") newPassword: String
    ): ResponseWrapper<Any>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @Multipart
    @PATCH("api/User/update")
    suspend fun updateFcmToken(
        @Part("RegistrationId") registrationId: RequestBody,
        @Part("DeviceType") deviceType: RequestBody
    ): ResponseWrapper<Any>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @Multipart
    @PATCH("api/User/update")
    suspend fun updateProfilePicture(
        @Part image: MultipartBody.Part
    ): ResponseWrapper<Any>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @FormUrlEncoded
    @POST("api/User/ResetPassword")
    suspend fun recoveryPassword(
        @Field("NewPassword") newPassword: String
    ): ResponseWrapper<Any>

}