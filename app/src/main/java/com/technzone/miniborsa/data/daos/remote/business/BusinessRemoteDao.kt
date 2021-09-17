package com.technzone.miniborsa.data.daos.remote.business

import com.technzone.miniborsa.data.api.response.ResponseWrapper
import com.technzone.miniborsa.data.common.NetworkConstants
import com.technzone.miniborsa.data.models.business.business.OwnerBusiness
import com.technzone.miniborsa.data.models.general.ListWrapper
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface BusinessRemoteDao {

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("api/owner/Business")
    suspend fun getBusinessByType(
        @Query("Type") type: Int?,
        @Query("PageSize") pageSize: Int,
        @Query("PageNumber") pageNumber: Int
    ): ResponseWrapper<ListWrapper<OwnerBusiness>>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("api/owner/Business/{id}")
    suspend fun getBusinessById(
        @Path("id") id: Int?
    ): ResponseWrapper<OwnerBusiness>

}