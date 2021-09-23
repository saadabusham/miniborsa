package com.technzone.miniborsa.data.repos.common

import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.api.response.ResponseWrapper
import com.technzone.miniborsa.data.models.general.ListWrapper
import com.technzone.miniborsa.data.models.investor.Business
import com.technzone.miniborsa.data.models.investor.investors.Investor
import com.technzone.miniborsa.data.models.investor.investors.InvestorFilter
import com.technzone.miniborsa.data.models.news.BusinessNews
import com.technzone.miniborsa.data.models.notification.Notification
import retrofit2.http.Path
import retrofit2.http.Query


interface CommonRepo {

    suspend fun getNotifications(
        pageSize: Int,
        pageNumber: Int
    ): APIResource<ResponseWrapper<ListWrapper<Notification>>>

    suspend fun getBlogs(
        pageSize: Int,
        pageNumber: Int,
        banner: Boolean,
        searchTxt: String?=""
    ): APIResource<ResponseWrapper<ListWrapper<BusinessNews>>>

    suspend fun getBlogDetails(
        id: Int
    ): APIResource<ResponseWrapper<BusinessNews>>
}