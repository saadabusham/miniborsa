package com.technzone.minibursa.ui.core.notification.viewmodels

import androidx.lifecycle.liveData
import com.technzone.minibursa.data.api.response.APIResource
import com.technzone.minibursa.data.common.Constants
import com.technzone.minibursa.data.repos.common.CommonRepo
import com.technzone.minibursa.ui.base.viewmodel.BaseViewModel
import com.technzone.minibursa.utils.pref.SharedPreferencesUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val commonRepo: CommonRepo,
    private val sharedPreferencesUtil: SharedPreferencesUtil
) : BaseViewModel() {

    fun getNotifications(pageNumber: Int) = liveData {
        emit(APIResource.loading())
        val response = commonRepo.getNotifications(pageSize = Constants.PAGE_SIZE, pageNumber)
        emit(response)
    }
    fun clearNewNotifications(){
        sharedPreferencesUtil.setIsNewNotifications(false)
    }
}