package com.technzone.miniborsa.data.repos.business

import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.api.response.ResponseWrapper
import com.technzone.miniborsa.data.models.business.business.OwnerBusiness
import com.technzone.miniborsa.data.models.business.businessrequest.BusinessRequest
import com.technzone.miniborsa.data.models.general.ListWrapper
import okhttp3.MultipartBody

interface BusinessRepo {

    suspend fun getBusinessByType(
        type: List<Int>,
        pageSize: Int,
        pageNumber: Int
    ): APIResource<ResponseWrapper<ListWrapper<OwnerBusiness>>>

    suspend fun getBusinessById(
        id: Int?
    ): APIResource<ResponseWrapper<OwnerBusiness>>

    suspend fun getRequestBusiness(
        id: Int
    ): APIResource<ResponseWrapper<BusinessRequest>>

    suspend fun requestBusiness(
        businessRequest: BusinessRequest
    ): APIResource<ResponseWrapper<Any>>

    suspend fun updateBusinessRequest(
        businessRequest: BusinessRequest
    ): APIResource<ResponseWrapper<Any>>

    suspend fun addRequestFiles(
        id: Int?,
        list: List<MultipartBody.Part>
    ): APIResource<ResponseWrapper<Any>>

    suspend fun deleteRequestFiles(
        id: Int?
    ): APIResource<ResponseWrapper<Any>>

    suspend fun addRequestImage(
        id: Int?,
        list: List<MultipartBody.Part>
    ): APIResource<ResponseWrapper<Any>>

    suspend fun deleteRequestImage(
        id: Int?
    ): APIResource<ResponseWrapper<Any>>

    suspend fun sendBusinessRequest(
        id: Int?
    ): APIResource<ResponseWrapper<Any>>
}