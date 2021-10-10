package com.technzone.minibursa.data.repos.searchbusiness

import com.technzone.minibursa.data.api.response.ResponseHandler
import com.technzone.minibursa.data.daos.local.searchbusiness.SearchedBusinessLocalDao
import com.technzone.minibursa.data.models.investor.Business
import com.technzone.minibursa.data.repos.base.BaseRepo
import javax.inject.Inject

class SearchedBusinessRepoImp @Inject constructor(
    responseHandler: ResponseHandler,
    private val businessLocalDao: SearchedBusinessLocalDao
) : BaseRepo(responseHandler), SearchedBusinessRepo {

    override suspend fun saveBusiness(business: Business) {
        businessLocalDao.saveBusiness(business)
    }

    override suspend fun saveBusinesses(businesses: List<Business>) {
        businessLocalDao.saveBusinesses(businesses)
    }

    override suspend fun getBusiness(): List<Business> {
        return businessLocalDao.getBusiness()
    }

    override suspend fun getBusiness(id: Int): Business {
        return businessLocalDao.getBusiness(id)
    }

    override suspend fun deleteBusiness(id: Int) {
        businessLocalDao.deleteBusiness(id)
    }

    override suspend fun clearBusinesses() {
        businessLocalDao.clearBusinesses()
    }


}