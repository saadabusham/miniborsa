package com.technzone.miniborsa.data.daos.remote.common

import com.technzone.miniborsa.data.api.response.ResponseWrapper
import com.technzone.miniborsa.data.common.NetworkConstants
import com.technzone.miniborsa.data.models.general.ListWrapper
import com.technzone.miniborsa.data.models.notification.Notification
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CommonRemoteDao {

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("api/user/notification")
    suspend fun getNotifications(
        @Query("PageSize") pageSize: Int,
        @Query("PageNumber") pageNumber: Int
    ): ResponseWrapper<ListWrapper<Notification>>

}