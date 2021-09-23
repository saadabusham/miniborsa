package com.technzone.miniborsa.ui.investor.news.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.common.Constants
import com.technzone.miniborsa.data.models.news.BusinessNews
import com.technzone.miniborsa.data.repos.common.CommonRepo
import com.technzone.miniborsa.data.repos.user.UserRepo
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val commonRepo: CommonRepo
) : BaseViewModel() {

    var blogToView: MutableLiveData<BusinessNews>? = MutableLiveData()
    var blogId: Int = -1
    val searchText: MutableLiveData<String> = MutableLiveData()

    fun getBannerBlogs(
    ) = liveData {
        emit(APIResource.loading())
        val response =
            commonRepo.getBlogs(
                pageNumber = 1,
                pageSize = Constants.PAGE_SIZE,
                banner = true
            )
        emit(response)
    }

    fun getBlogs(
        pageNumber: Int
    ) = liveData {
        emit(APIResource.loading())
        val response =
            commonRepo.getBlogs(
                pageNumber = pageNumber,
                pageSize = Constants.PAGE_SIZE,
                banner = true
            )
        emit(response)
    }

    fun getBlogDetails(
        blogId: Int
    ) = liveData {
        emit(APIResource.loading())
        val response =
            commonRepo.getBlogDetails(blogId)
        emit(response)
    }

}