package com.technzone.miniborsa.ui.business.listingpreview.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.models.business.business.OwnerBusiness
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

    var business: OwnerBusiness? = null
    val percentage: MutableLiveData<Int> = MutableLiveData(0)
    var hasBusiness: Boolean = false
    var companyDraft: Boolean = false
    var businessDraft: Boolean = false

    private fun isHasBusiness(): Boolean {
        return hasBusiness && !companyDraft
    }

    fun sendRequestBusiness(businessId: Int) = liveData {
        emit(APIResource.loading())
        val response =
            if (isHasBusiness())
                businessRepo.sendBusinessRequest(businessId)
            else businessRepo.sendCompanyRequest()
        emit(response)
    }

    fun deleteCompanyRequest() = liveData {
        emit(APIResource.loading())
        val response =
            businessRepo.deleteCompanyRequest()
        emit(response)
    }

    fun deleteBusinessRequest(id:Int) = liveData {
        emit(APIResource.loading())
        val response =
            businessRepo.deleteBusinessRequest(id)
        emit(response)
    }

}