package com.technzone.miniborsa.data.daos.remote.common

import com.technzone.miniborsa.data.api.response.ResponseWrapper
import com.technzone.miniborsa.data.common.NetworkConstants
import com.technzone.miniborsa.data.models.general.ListWrapper
import com.technzone.miniborsa.data.models.news.BusinessNews
import com.technzone.miniborsa.data.models.notification.Notification
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface CommonRemoteDao {

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("api/user/notification")
    suspend fun getNotifications(
        @Query("PageSize") pageSize: Int,
        @Query("PageNumber") pageNumber: Int
    ): ResponseWrapper<ListWrapper<Notification>>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("api/Blog")
    suspend fun getBlogs(
        @Query("PageSize") pageSize: Int,
        @Query("PageNumber") pageNumber: Int,
        @Query("banner") banner: Boolean,
        @Query("title") searchTxt: String?
    ): ResponseWrapper<ListWrapper<BusinessNews>>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("api/Blog/{id}")
    suspend fun getBlogDetails(
        @Path("id") id: Int
    ): ResponseWrapper<BusinessNews>

}