package com.technzone.miniborsa.data.daos.remote.business

import com.technzone.miniborsa.data.api.response.ResponseWrapper
import com.technzone.miniborsa.data.common.NetworkConstants
import com.technzone.miniborsa.data.models.business.business.OwnerBusiness
import com.technzone.miniborsa.data.models.business.businessrequest.BusinessRequest
import com.technzone.miniborsa.data.models.general.ListWrapper
import okhttp3.MultipartBody
import retrofit2.http.*

interface BusinessRemoteDao {

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("api/owner/Business/request/search")
    suspend fun getBusinessByType(
        @Query("statuses") type: List<Int>?,
        @Query("PageSize") pageSize: Int,
        @Query("PageNumber") pageNumber: Int
    ): ResponseWrapper<ListWrapper<OwnerBusiness>>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("api/owner/Business/request/{id}")
    suspend fun getBusinessById(
        @Path("id") id: Int?
    ): ResponseWrapper<OwnerBusiness>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("api/owner/Business/request/{id}")
    suspend fun getRequestBusiness(
        @Path("id") id: Int
    ): ResponseWrapper<OwnerBusiness>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @POST("api/owner/Business/request")
    suspend fun requestBusiness(
        @Body businessRequest: BusinessRequest
    ): ResponseWrapper<Int>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @PUT("api/owner/Business/request")
    suspend fun updateBusinessRequest(
        @Body businessRequest: BusinessRequest
    ): ResponseWrapper<Any>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @Multipart
    @POST("api/owner/Business/request/file")
    suspend fun addBusinessRequestFiles(
        @Field("id") id: Int?,
        @Part list: List<MultipartBody.Part>
    ): ResponseWrapper<Any>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @DELETE("api/owner/Business/request/file/{id}")
    suspend fun deleteBusinessRequestFiles(
        @Path("id") id: Int?
    ): ResponseWrapper<Any>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @Multipart
    @POST("api/owner/Business/request/image")
    suspend fun addBusinessRequestImage(
        @Field("id") id: Int?,
        @Part list: List<MultipartBody.Part>
    ): ResponseWrapper<Any>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @DELETE("api/owner/Business/request/image/{id}")
    suspend fun deleteBusinessRequestImage(
        @Path("id") id: Int?
    ): ResponseWrapper<Any>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @PATCH("api/owner/Business/request/send/{id}")
    suspend fun sendBusinessRequest(
        @Path("id") id: Int?
    ): ResponseWrapper<Any>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("api/owner/company/request")
    suspend fun getRequestCompany(
    ): ResponseWrapper<OwnerBusiness>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @FormUrlEncoded
    @POST("api/owner/company/request")
    suspend fun requestCompany(
        @Field("Name") name: String
    ): ResponseWrapper<Int>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @PUT("api/owner/company/request")
    suspend fun updateCompanyRequest(
        @Body businessRequest: BusinessRequest
    ): ResponseWrapper<Any>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @Multipart
    @POST("api/owner/company/request/file")
    suspend fun addCompanyRequestFiles(
        @Query("id") id: Int?,
        @Part list: List<MultipartBody.Part>
    ): ResponseWrapper<Any>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @DELETE("api/owner/company/request/file/{id}")
    suspend fun deleteCompanyRequestFiles(
        @Path("id") id: Int?
    ): ResponseWrapper<Any>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @Multipart
    @POST("api/owner/company/request/image")
    suspend fun addCompanyRequestImage(
        @Query("id") id: Int?,
        @Part list: List<MultipartBody.Part>
    ): ResponseWrapper<Any>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @DELETE("api/owner/company/request/image/{id}")
    suspend fun deleteCompanyRequestImage(
        @Path("id") id: Int?
    ): ResponseWrapper<Any>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @PATCH("api/owner/company/request/send/{id}")
    suspend fun sendCompanyRequest(
        @Path("id") id: Int?
    ): ResponseWrapper<Any>

}