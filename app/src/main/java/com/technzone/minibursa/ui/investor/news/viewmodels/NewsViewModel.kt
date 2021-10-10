package com.technzone.minibursa.ui.investor.news.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.technzone.minibursa.data.api.response.APIResource
import com.technzone.minibursa.data.common.Constants
import com.technzone.minibursa.data.enums.NewsSectionEnums
import com.technzone.minibursa.data.models.news.BusinessNews
import com.technzone.minibursa.data.repos.common.CommonRepo
import com.technzone.minibursa.data.repos.user.UserRepo
import com.technzone.minibursa.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val commonRepo: CommonRepo
) : BaseViewModel() {

    var blogToView: MutableLiveData<BusinessNews>? = MutableLiveData()
    var blogId: Int = -1
    val searchText: MutableLiveData<String> = MutableLiveData("")

    fun getBannerBlogs(
    ) = liveData {
        emit(APIResource.loading())
        val response =
            commonRepo.getBlogs(
                pageNumber = 1,
                pageSize = Constants.PAGE_SIZE,
                section = NewsSectionEnums.ALL.value,
                isPinned = true
            )
        emit(response)
    }

    fun getBlogs(
        pageNumber: Int,
        section:Int = NewsSectionEnums.ALL.value
    ) = liveData {
        emit(APIResource.loading())
        val response =
            commonRepo.getBlogs(
                pageNumber = pageNumber,
                pageSize = Constants.PAGE_SIZE,
                section = section,
                searchTxt = searchText.value,
                isPinned = false
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