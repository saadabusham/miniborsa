package com.technzone.miniborsa.ui.business.listingpreview.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.models.business.businessrequest.BusinessRequest
import com.technzone.miniborsa.data.pref.user.UserPref
import com.technzone.miniborsa.data.repos.business.BusinessRepo
import com.technzone.miniborsa.data.repos.user.UserRepo
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import com.technzone.miniborsa.utils.pref.SharedPreferencesUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListingPreviewViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val userPref: UserPref,
    private val sharedPreferencesUtil: SharedPreferencesUtil,
    private val businessRepo: BusinessRepo
) : BaseViewModel() {

    var businessRequest:BusinessRequest? = null
    val percentage: MutableLiveData<Int> = MutableLiveData(0)

    fun updateRequest() = liveData {
        emit(APIResource.loading())
        val response =
            businessRepo.updateBusinessRequest(businessRequest?:BusinessRequest())
        emit(response)
    }

    fun sendRequest() = liveData {
        emit(APIResource.loading())
        val response =
            businessRepo.sendBusinessRequest(businessRequest?.businessId)
        emit(response)
    }
}