package com.technzone.miniborsa.data.daos.remote.common

import com.technzone.miniborsa.data.api.response.ResponseWrapper
import com.technzone.miniborsa.data.common.NetworkConstants
import com.technzone.miniborsa.data.models.FaqsResponse
import com.technzone.miniborsa.data.models.general.ListWrapper
import com.technzone.miniborsa.data.models.investor.Business
import com.technzone.miniborsa.data.models.investor.request.FavoriteRequest
import com.technzone.miniborsa.data.models.news.BusinessNews
import com.technzone.miniborsa.data.models.notification.Notification
import retrofit2.http.*

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
        @Query("Section") section: Int?,
        @Query("EnglishTitle") searchTxt: String?,
        @Query("IsPinned") isPinned: Boolean?
    ): ResponseWrapper<ListWrapper<BusinessNews>>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("api/Blog/{id}")
    suspend fun getBlogDetails(
        @Path("id") id: Int
    ): ResponseWrapper<BusinessNews>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("api/user/favorite")
    suspend fun getFavorites(
        @Query("PageSize") pageSize: Int,
        @Query("PageNumber") pageNumber: Int
    ): ResponseWrapper<ListWrapper<Business>>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @POST("api/user/favorite")
    suspend fun addFavorite(
        @Body favoriteRequest: FavoriteRequest
    ): ResponseWrapper<Any>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @DELETE("api/user/favorite")
    suspend fun removeFavorite(
        @Body favoriteRequest: FavoriteRequest
    ): ResponseWrapper<Any>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:true")
    @GET("api/faq")
    suspend fun getFaqs(
        @Query("PageSize") pageSize: Int,
        @Query("PageNumber") pageNumber: Int
    ): ResponseWrapper<ListWrapper<FaqsResponse>>


}