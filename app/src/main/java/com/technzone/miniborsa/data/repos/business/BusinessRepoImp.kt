package com.technzone.miniborsa.data.repos.business

import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.api.response.ResponseHandler
import com.technzone.miniborsa.data.api.response.ResponseWrapper
import com.technzone.miniborsa.data.daos.remote.business.BusinessRemoteDao
import com.technzone.miniborsa.data.models.business.business.OwnerBusiness
import com.technzone.miniborsa.data.models.general.ListWrapper
import com.technzone.miniborsa.data.repos.base.BaseRepo
import javax.inject.Inject

class BusinessRepoImp @Inject constructor(
    responseHandler: ResponseHandler,
    private val businessRemoteDao: BusinessRemoteDao
) : BaseRepo(responseHandler), BusinessRepo {

    override suspend fun getBusinessByType(
        type: Int?,
        pageSize: Int,
        pageNumber: Int
    ): APIResource<ResponseWrapper<ListWrapper<OwnerBusiness>>> {
        return try {
            responseHandle.handleSuccess(
                businessRemoteDao.getBusinessByType(
                    type,
                    pageSize,
                    pageNumber
                )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getBusinessById(id: Int?): APIResource<ResponseWrapper<OwnerBusiness>> {
        return try {
            responseHandle.handleSuccess(
                businessRemoteDao.getBusinessById(
                    id
                )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

}