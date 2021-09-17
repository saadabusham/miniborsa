package com.technzone.miniborsa.ui.business.businessmain.viewmodels

import androidx.lifecycle.liveData
import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.repos.business.BusinessRepo
import com.technzone.miniborsa.data.repos.user.UserRepo
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BusinessMainViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val businessRepo: BusinessRepo
) : BaseViewModel() {

    fun getPendingListing() = liveData {
        emit(APIResource.loading())
        val response = businessRepo.getBusinessByType(1, pageSize = 1000, pageNumber = 1)
        emit(response)
    }
    fun getListing() = liveData {
        emit(APIResource.loading())
        val response = businessRepo.getBusinessByType(2, pageSize = 1000, pageNumber = 1)
        emit(response)
    }
}