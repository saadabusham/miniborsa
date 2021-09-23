package com.technzone.miniborsa.data.repos.common

import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.api.response.ResponseHandler
import com.technzone.miniborsa.data.api.response.ResponseWrapper
import com.technzone.miniborsa.data.daos.remote.common.CommonRemoteDao
import com.technzone.miniborsa.data.models.general.ListWrapper
import com.technzone.miniborsa.data.models.news.BusinessNews
import com.technzone.miniborsa.data.models.notification.Notification
import com.technzone.miniborsa.data.repos.base.BaseRepo
import javax.inject.Inject

class CommonRepoImp @Inject constructor(
    responseHandler: ResponseHandler,
    private val commonRemoteDao: CommonRemoteDao
) : BaseRepo(responseHandler), CommonRepo {

    override suspend fun getNotifications(
        pageSize: Int,
        pageNumber: Int
    ): APIResource<ResponseWrapper<ListWrapper<Notification>>> {
        return try {
            responseHandle.handleSuccess(
                commonRemoteDao.getNotifications(pageSize, pageNumber)
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getBlogs(
        pageSize: Int,
        pageNumber: Int,
        banner: Boolean,
        searchTxt: String?
    ): APIResource<ResponseWrapper<ListWrapper<BusinessNews>>> {
        return try {
            responseHandle.handleSuccess(
                commonRemoteDao.getBlogs(pageSize, pageNumber, banner,searchTxt)
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getBlogDetails(id: Int): APIResource<ResponseWrapper<BusinessNews>> {
        return try {
            responseHandle.handleSuccess(
                commonRemoteDao.getBlogDetails(id)
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }
}