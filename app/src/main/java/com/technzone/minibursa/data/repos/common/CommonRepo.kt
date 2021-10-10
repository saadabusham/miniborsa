package com.technzone.minibursa.data.repos.common

import com.technzone.minibursa.data.api.response.APIResource
import com.technzone.minibursa.data.api.response.ResponseWrapper
import com.technzone.minibursa.data.models.FaqsResponse
import com.technzone.minibursa.data.models.general.ListWrapper
import com.technzone.minibursa.data.models.investor.Business
import com.technzone.minibursa.data.models.investor.request.FavoriteRequest
import com.technzone.minibursa.data.models.news.BusinessNews
import com.technzone.minibursa.data.models.notification.Notification


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
        isPinned: Boolean? = true
    ): APIResource<ResponseWrapper<ListWrapper<BusinessNews>>>

    suspend fun getBlogDetails(
        id: Int
    ): APIResource<ResponseWrapper<BusinessNews>>

    suspend fun getFavorites(
        pageSize: Int,
        pageNumber: Int
    ): APIResource<ResponseWrapper<ListWrapper<Business>>>

    suspend fun getFavoriteIds(
    ): APIResource<ResponseWrapper<List<Int>>>

    suspend fun addFavorite(
        favoriteRequest: FavoriteRequest
    ): APIResource<ResponseWrapper<Any>>

    suspend fun removeFavorite(
        favoriteRequest: FavoriteRequest
    ): APIResource<ResponseWrapper<Any>>

    suspend fun getFaqs(
        pageSize: Int,
        pageNumber: Int
    ): APIResource<ResponseWrapper<ListWrapper<FaqsResponse>>>
}