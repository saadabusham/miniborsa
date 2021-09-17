package com.technzone.miniborsa.data.repos.business

import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.api.response.ResponseWrapper
import com.technzone.miniborsa.data.models.business.business.OwnerBusiness
import com.technzone.miniborsa.data.models.general.ListWrapper
import retrofit2.http.Path

interface BusinessRepo {

    suspend fun getBusinessByType(
        type: Int?,
        pageSize: Int,
        pageNumber: Int
    ): APIResource<ResponseWrapper<ListWrapper<OwnerBusiness>>>

    suspend fun getBusinessById(
        id: Int?
    ): APIResource<ResponseWrapper<OwnerBusiness>>
}