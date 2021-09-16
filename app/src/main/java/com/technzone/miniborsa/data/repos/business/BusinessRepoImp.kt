package com.technzone.miniborsa.data.repos.business

import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.api.response.ResponseHandler
import com.technzone.miniborsa.data.api.response.ResponseWrapper
import com.technzone.miniborsa.data.daos.remote.business.BusinessRemoteDao
import com.technzone.miniborsa.data.models.investor.investors.Investor
import com.technzone.miniborsa.data.repos.base.BaseRepo
import javax.inject.Inject

class BusinessRepoImp @Inject constructor(
    responseHandler: ResponseHandler,
    private val businessRemoteDao: BusinessRemoteDao
) : BaseRepo(responseHandler), BusinessRepo {

    override suspend fun getInvestors(): APIResource<ResponseWrapper<List<Investor>>> {
        return try {
            responseHandle.handleSuccess(
                businessRemoteDao.getInvestors()
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }
}