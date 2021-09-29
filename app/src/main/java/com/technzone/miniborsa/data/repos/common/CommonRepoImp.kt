package com.technzone.miniborsa.data.repos.common

import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.api.response.ResponseHandler
import com.technzone.miniborsa.data.api.response.ResponseWrapper
import com.technzone.miniborsa.data.daos.remote.common.CommonRemoteDao
import com.technzone.miniborsa.data.models.FaqsResponse
import com.technzone.miniborsa.data.models.general.ListWrapper
import com.technzone.miniborsa.data.models.investor.Business
import com.technzone.miniborsa.data.models.investor.request.FavoriteRequest
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
        section: Int?,
        searchTxt: String?,
        isPinned: Boolean?
    ): APIResource<ResponseWrapper<ListWrapper<BusinessNews>>> {
        return try {
            responseHandle.handleSuccess(
                commonRemoteDao.getBlogs(pageSize, pageNumber, section,searchTxt,isPinned)
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

    override suspend fun getFavorites(
        pageSize: Int,
        pageNumber: Int
    ): APIResource<ResponseWrapper<ListWrapper<Business>>> {
        return try {
            responseHandle.handleSuccess(
                commonRemoteDao.getFavorites(
                    pageSize,
                    pageNumber
                )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getFavoriteIds(): APIResource<ResponseWrapper<List<Int>>> {
        return try {
            responseHandle.handleSuccess(
                commonRemoteDao.getFavoriteIds()
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun addFavorite(favoriteRequest: FavoriteRequest): APIResource<ResponseWrapper<Any>> {
        return try {
            responseHandle.handleSuccess(
                commonRemoteDao.addFavorite(favoriteRequest)
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun removeFavorite(favoriteRequest: FavoriteRequest): APIResource<ResponseWrapper<Any>> {
        return try {
            responseHandle.handleSuccess(
                commonRemoteDao.removeFavorite(favoriteRequest)
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getFaqs(
        pageSize: Int,
        pageNumber: Int
    ): APIResource<ResponseWrapper<ListWrapper<FaqsResponse>>> {
        return try {
            responseHandle.handleSuccess(commonRemoteDao.getFaqs(pageSize, pageNumber))
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

}