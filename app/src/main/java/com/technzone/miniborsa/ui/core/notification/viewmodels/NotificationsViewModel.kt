package com.technzone.miniborsa.ui.core.notification.viewmodels

import androidx.lifecycle.liveData
import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.common.Constants
import com.technzone.miniborsa.data.repos.common.CommonRepo
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val commonRepo: CommonRepo
) : BaseViewModel() {

    fun getNotifications(pageNumber: Int) = liveData {
        emit(APIResource.loading())
        val response = commonRepo.getNotifications(pageSize = Constants.PAGE_SIZE, pageNumber)
        emit(response)
    }
}