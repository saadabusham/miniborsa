package com.technzone.miniborsa.ui.business.businessdraft.viewmodels

import androidx.lifecycle.liveData
import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.repos.business.BusinessRepo
import com.technzone.miniborsa.data.repos.user.UserRepo
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BusinessDraftViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val businessRepo: BusinessRepo
) : BaseViewModel() {

    fun getPendingListing() = liveData {
        emit(APIResource.loading())
        val response =
            businessRepo.getRequestCompany()
        emit(response)
    }
    fun deleteCompanyRequest() = liveData {
        emit(APIResource.loading())
        val response =
            businessRepo.deleteCompanyRequest()
        emit(response)
    }

}