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
    @POST("api/user/register")
    suspend fun register(
        @Field("Password") password: String,
        @Field("FirstName") firstName: String,
        @Field("LastName") lastName: String,
        @Field("PhoneNumber") phoneNumber: String,
        @Field("Email") email: String,
        @Field("RegistrationId") registrationId: String,
        @Field("DeviceType") deviceType: Int,
        @Field("ApplicationType") applicationType: Int
    ): ResponseWrapper<String>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:true")
    @POST("api/user/forgetPassword/{email}")
    suspend fun forgetPassword(
        @Path("email") email: String
    ): ResponseWrapper<String>


    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:true")
    @FormUrlEncoded
    @POST("api/user/verify")
    suspend fun verify(
        @Field("UserId") userId: String,
        @Field("VerificationCode") verificationCode: String
    ): ResponseWrapper<UserDetailsResponseModel>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:true")
    @GET("api/user/resendCode")
    suspend fun resendCode(
        @Query("PhoneNumber") phoneNumber: String
    ): ResponseWrapper<String>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @FormUrlEncoded
    @POST("api/user/refreshToken")
    suspend fun refreshToken(
        @Field("RefreshToken") refreshToken: String
    ): ResponseWrapper<UserDetailsResponseModel>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("api/user")
    suspend fun getProfile(
    ): ResponseWrapper<UserDetailsResponseModel>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @Multipart
    @PATCH("api/user/update")
    suspend fun updateProfile(
        @Part("Email") email: RequestBody?,
        @Part("FirstName") firstName: RequestBody,
        @Part("LastName") lastName: RequestBody,
        @Part("PhoneNumber") phoneNumber: RequestBody
    ): ResponseWrapper<Any>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @FormUrlEncoded
    @POST("api/user/newPassword")
    suspend fun updatePassword(
        @Field("OldPassword") oldPassword: String,
        @Field("Password") newPassword: String
    ): ResponseWrapper<Any>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @FormUrlEncoded
    @POST("api/user/newPassword")
    suspend fun resetPassword(
        @Field("Password") newPassword: String
    ): ResponseWrapper<Any>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @Multipart
    @PATCH("api/user/update")
    suspend fun updateFcmToken(
        @Part("RegistrationId") registrationId: RequestBody,
        @Part("DeviceType") deviceType: RequestBody
    ): ResponseWrapper<Any>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @Multipart
    @PATCH("api/user/update")
    suspend fun updateProfilePicture(
        @Part image: MultipartBody.Part
    ): ResponseWrapper<Any>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @FormUrlEncoded
    @POST("api/Investors")
    suspend fun registerInvestor(
        @Field("JobTitle") jobTitle: String,
        @Field("InvestmentBudget") investmentBudget: Double,
        @Field("InvestmentBudgetNA") investmentBudgetNA: Boolean,
        @Field("Countries") countries: List<Int>,
        @Field("Categories") categories: List<Int>
    ): ResponseWrapper<Any>

}