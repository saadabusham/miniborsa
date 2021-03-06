package com.technzone.minibursa.data.repos.business

import com.technzone.minibursa.data.api.response.APIResource
import com.technzone.minibursa.data.api.response.ResponseWrapper
import com.technzone.minibursa.data.models.business.business.OwnerBusiness
import com.technzone.minibursa.data.models.business.businessrequest.BusinessRequest
import com.technzone.minibursa.data.models.business.request.BusinessSearchRequest
import com.technzone.minibursa.data.models.general.ListWrapper
import okhttp3.MultipartBody

interface BusinessRepo {

    suspend fun getOwnerBusiness(
        pageSize : Int,
        pageNumber : Int
    ): APIResource<ResponseWrapper<ListWrapper<OwnerBusiness>>>

    suspend fun getBusinessByType(
        businessSearchRequest: BusinessSearchRequest
    ): APIResource<ResponseWrapper<ListWrapper<OwnerBusiness>>>

    suspend fun getBusinessById(
        id: Int?
    ): APIResource<ResponseWrapper<OwnerBusiness>>

    suspend fun getRequestBusiness(
        id: Int
    ): APIResource<ResponseWrapper<OwnerBusiness>>

    suspend fun requestBusiness(
        businessRequest: BusinessRequest
    ): APIResource<ResponseWrapper<Int>>

    suspend fun deleteBusinessRequest(
        id:Int
    ): APIResource<ResponseWrapper<Any>>

    suspend fun updateBusinessRequest(
        businessRequest: BusinessRequest
    ): APIResource<ResponseWrapper<Any>>

    suspend fun addBusinessRequestFiles(
        id: Int?,
        list: List<MultipartBody.Part>
    ): APIResource<ResponseWrapper<Any>>

    suspend fun deleteBusinessRequestFiles(
        id: Int?
    ): APIResource<ResponseWrapper<Any>>

    suspend fun addBusinessIcon(
        id: Int?,
        icon: MultipartBody.Part
    ): APIResource<ResponseWrapper<Any>>

    suspend fun addBusinessRequestImage(
        id: Int?,
        list: List<MultipartBody.Part>
    ): APIResource<ResponseWrapper<Any>>

    suspend fun deleteBusinessRequestImage(
        id: Int?
    ): APIResource<ResponseWrapper<Any>>

    suspend fun sendBusinessRequest(
        id: Int?
    ): APIResource<ResponseWrapper<Any>>


    suspend fun getRequestCompany(
    ): APIResource<ResponseWrapper<OwnerBusiness>>

    suspend fun requestCompany(
        name: String
    ): APIResource<ResponseWrapper<Int>>

    suspend fun updateCompanyRequest(
        businessRequest: BusinessRequest
    ): APIResource<ResponseWrapper<Any>>

    suspend fun addCompanyRequestFiles(
        id: Int?,
        list: List<MultipartBody.Part>
    ): APIResource<ResponseWrapper<Any>>

    suspend fun deleteCompanyRequestFiles(
        id: Int?
    ): APIResource<ResponseWrapper<Any>>

    suspend fun addCompanyIcon(
        id: Int?,
        icon: MultipartBody.Part
    ): APIResource<ResponseWrapper<Any>>

    suspend fun deleteCompanyIcon(
    ): APIResource<ResponseWrapper<Any>>

    suspend fun addCompanyRequestImage(
        id: Int?,
        list: List<MultipartBody.Part>
    ): APIResource<ResponseWrapper<Any>>

    suspend fun deleteCompanyRequestImage(
        id: Int?
    ): APIResource<ResponseWrapper<Any>>

    suspend fun sendCompanyRequest(
    ): APIResource<ResponseWrapper<Any>>

    suspend fun deleteCompanyRequest(
    ): APIResource<ResponseWrapper<Any>>
}