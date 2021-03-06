package com.technzone.minibursa.data.daos.remote.common

import com.technzone.minibursa.data.api.response.ResponseWrapper
import com.technzone.minibursa.data.common.NetworkConstants
import com.technzone.minibursa.data.models.FaqsResponse
import com.technzone.minibursa.data.models.general.ListWrapper
import com.technzone.minibursa.data.models.investor.Business
import com.technzone.minibursa.data.models.investor.request.FavoriteRequest
import com.technzone.minibursa.data.models.news.BusinessNews
import com.technzone.minibursa.data.models.notification.Notification
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
    @GET("api/user/favorite/FavIds")
    suspend fun getFavoriteIds(
    ): ResponseWrapper<List<Int>>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @POST("api/user/favorite")
    suspend fun addFavorite(
        @Body favoriteRequest: FavoriteRequest
    ): ResponseWrapper<Any>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
//    @DELETE("api/user/favorite")
    @HTTP(method = "DELETE", path = "api/user/favorite", hasBody = true)
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