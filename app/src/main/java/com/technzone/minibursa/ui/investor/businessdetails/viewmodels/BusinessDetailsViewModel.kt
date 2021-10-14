package com.technzone.minibursa.ui.investor.businessdetails.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.technzone.minibursa.data.api.response.APIResource
import com.technzone.minibursa.data.enums.UserRoleEnums
import com.technzone.minibursa.data.models.investor.Business
import com.technzone.minibursa.data.pref.favorite.FavoritePref
import com.technzone.minibursa.data.repos.common.CommonRepo
import com.technzone.minibursa.data.repos.investors.InvestorsRepo
import com.technzone.minibursa.data.repos.twilio.TwilioRepo
import com.technzone.minibursa.data.repos.user.UserRepo
import com.technzone.minibursa.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BusinessDetailsViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val investorsRepo: InvestorsRepo,
    private val commonRepo: CommonRepo,
    private val favoritePref: FavoritePref,
    private val twilioRepo: TwilioRepo
) : BaseViewModel() {
    var businessToView: MutableLiveData<Business> = MutableLiveData(null)
    val isInvestor: MutableLiveData<Boolean> = MutableLiveData(getIsInvestor())
    fun getBusiness(
        id: Int
    ) = liveData {
        emit(APIResource.loading())
        val response =
            investorsRepo.getBusinessById(id)
        favoritePref.getFavoriteList().singleOrNull { it == response.data?.data?.id }?.let {
            response.data?.data?.isFavorite = true
        }
        emit(response)
    }

    fun getChanelId(
    ) = liveData {
        emit(APIResource.loading())
        val response =
            twilioRepo.getBusinessChannelId(businessToView.value?.id ?: 0)
        emit(response)
    }

    fun getIsInvestor() =
        userRepo.getUser()?.roles?.singleOrNull { it.role == UserRoleEnums.INVESTOR_ROLE.value } != null
}