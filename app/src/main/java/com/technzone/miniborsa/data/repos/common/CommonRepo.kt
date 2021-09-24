package com.technzone.miniborsa.data.repos.common

import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.api.response.ResponseWrapper
import com.technzone.miniborsa.data.models.general.ListWrapper
import com.technzone.miniborsa.data.models.investor.request.FavoriteRequest
import com.technzone.miniborsa.data.models.news.BusinessNews
import com.technzone.miniborsa.data.models.notification.Notification


interface CommonRepo {

    suspend fun getNotifications(
        pageSize: Int,
        pageNumber: Int
    ): APIResource<ResponseWrapper<ListWrapper<Notification>>>

    suspend fun getBlogs(
        pageSize: Int,
        pageNumber: Int,
        section: Int?,
        searchTxt: String? = "",
        type: Int?
    ): APIResource<ResponseWrapper<ListWrapper<BusinessNews>>>

    suspend fun getBlogDetails(
        id: Int
    ): APIResource<ResponseWrapper<BusinessNews>>

    suspend fun addFavorite(
        favoriteRequest: FavoriteRequest
    ): APIResource<ResponseWrapper<Any>>

    suspend fun removeFavorite(
        favoriteRequest: FavoriteRequest
    ): APIResource<ResponseWrapper<Any>>

}