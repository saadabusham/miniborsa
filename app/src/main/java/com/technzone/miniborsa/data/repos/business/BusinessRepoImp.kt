package com.technzone.miniborsa.data.repos.business

import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.api.response.ResponseHandler
import com.technzone.miniborsa.data.api.response.ResponseWrapper
import com.technzone.miniborsa.data.daos.remote.business.BusinessRemoteDao
import com.technzone.miniborsa.data.models.business.business.OwnerBusiness
import com.technzone.miniborsa.data.models.business.businessrequest.BusinessRequest
import com.technzone.miniborsa.data.models.general.ListWrapper
import com.technzone.miniborsa.data.repos.base.BaseRepo
import okhttp3.MultipartBody
import javax.inject.Inject

class BusinessRepoImp @Inject constructor(
    responseHandler: ResponseHandler,
    private val businessRemoteDao: BusinessRemoteDao
) : BaseRepo(responseHandler), BusinessRepo {

    override suspend fun getBusinessByType(
        type: List<Int>,
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

    override suspend fun getRequestBusiness(id: Int): APIResource<ResponseWrapper<OwnerBusiness>> {
        return try {
            responseHandle.handleSuccess(
                businessRemoteDao.getRequestBusiness(id)
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun requestBusiness(businessRequest: BusinessRequest): APIResource<ResponseWrapper<Int>> {
        return try {
            responseHandle.handleSuccess(
                businessRemoteDao.requestBusiness(
                    businessRequest
                )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun updateBusinessRequest(businessRequest: BusinessRequest): APIResource<ResponseWrapper<Any>> {
        return try {
            responseHandle.handleSuccess(
                businessRemoteDao.updateBusinessRequest(
                    businessRequest
                )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun addBusinessRequestFiles(
        id: Int?,
        list: List<MultipartBody.Part>
    ): APIResource<ResponseWrapper<Any>> {
        return try {
            responseHandle.handleSuccess(
                businessRemoteDao.addBusinessRequestFiles(
                    id, list
                )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun deleteBusinessRequestFiles(id: Int?): APIResource<ResponseWrapper<Any>> {
        return try {
            responseHandle.handleSuccess(
                businessRemoteDao.deleteBusinessRequestFiles(
                    id
                )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun addBusinessRequestImage(
        id: Int?,
        list: List<MultipartBody.Part>
    ): APIResource<ResponseWrapper<Any>> {
        return try {
            responseHandle.handleSuccess(
                businessRemoteDao.addBusinessRequestImage(
                    id, list
                )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun deleteBusinessRequestImage(id: Int?): APIResource<ResponseWrapper<Any>> {
        return try {
            responseHandle.handleSuccess(
                businessRemoteDao.deleteBusinessRequestFiles(
                    id
                )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun sendBusinessRequest(id: Int?): APIResource<ResponseWrapper<Any>> {
        return try {
            responseHandle.handleSuccess(
                businessRemoteDao.sendBusinessRequest(
                    id
                )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getRequestCompany(): APIResource<ResponseWrapper<OwnerBusiness>> {
        return try {
            responseHandle.handleSuccess(
                businessRemoteDao.getRequestCompany()
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun requestCompany(name: String): APIResource<ResponseWrapper<Int>> {
        return try {
            responseHandle.handleSuccess(
                businessRemoteDao.requestCompany(name)
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun updateCompanyRequest(businessRequest: BusinessRequest): APIResource<ResponseWrapper<Any>> {
        return try {
            responseHandle.handleSuccess(
                businessRemoteDao.updateCompanyRequest(businessRequest)
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun addCompanyRequestFiles(
        id: Int?,
        list: List<MultipartBody.Part>
    ): APIResource<ResponseWrapper<Any>> {
        return try {
            responseHandle.handleSuccess(
                businessRemoteDao.addCompanyRequestFiles(id,list)
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun deleteCompanyRequestFiles(id: Int?): APIResource<ResponseWrapper<Any>> {
        return try {
            responseHandle.handleSuccess(
                businessRemoteDao.deleteCompanyRequestFiles(id)
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun addCompanyRequestImage(
        id: Int?,
        list: List<MultipartBody.Part>
    ): APIResource<ResponseWrapper<Any>> {
        return try {
            responseHandle.handleSuccess(
                businessRemoteDao.addCompanyRequestImage(id,list)
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun deleteCompanyRequestImage(id: Int?): APIResource<ResponseWrapper<Any>> {
        return try {
            responseHandle.handleSuccess(
                businessRemoteDao.deleteCompanyRequestImage(id)
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun sendCompanyRequest(id: Int?): APIResource<ResponseWrapper<Any>> {
        return try {
            responseHandle.handleSuccess(
                businessRemoteDao.sendCompanyRequest(id)
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

}