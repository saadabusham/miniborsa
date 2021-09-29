package com.technzone.miniborsa.ui.business.businessmain.viewmodels

import androidx.lifecycle.liveData
import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.enums.BusinessStatusEnums
import com.technzone.miniborsa.data.models.business.request.BusinessSearchRequest
import com.technzone.miniborsa.data.repos.business.BusinessRepo
import com.technzone.miniborsa.data.repos.user.UserRepo
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import com.technzone.miniborsa.utils.pref.SharedPreferencesUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BusinessMainViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val businessRepo: BusinessRepo,
    private val sharedPreferencesUtil: SharedPreferencesUtil
) : BaseViewModel() {

    var promotePrice = 50

    fun isNewNotification():Boolean {
        return sharedPreferencesUtil.getIsNewNotifications()
    }
    fun getPendingListing() = liveData {
        emit(APIResource.loading())
        val response =
            businessRepo.getBusinessByType(
                BusinessSearchRequest(
                    statuses = arrayListOf(
                        BusinessStatusEnums.DRAFT.value,
                        BusinessStatusEnums.NEW.value,
                        BusinessStatusEnums.REJECTED.value
                    ), pageSize = 1000, pageNumber = 1
                )
            )
        emit(response)
    }

    fun getListing() = liveData {
        emit(APIResource.loading())
        val response =
            businessRepo.getBusinessByType(
                BusinessSearchRequest(
                    statuses = arrayListOf(
                        BusinessStatusEnums.APPROVED.value
                    ), pageSize = 1000, pageNumber = 1
                )
            )
        emit(response)
    }

    fun deleteBusinessRequest(id:Int) = liveData {
        emit(APIResource.loading())
        val response =
            businessRepo.deleteBusinessRequest(id)
        emit(response)
    }
}