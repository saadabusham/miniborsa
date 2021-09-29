package com.technzone.miniborsa.ui.investor.businessdetails.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.enums.UserRoleEnums
import com.technzone.miniborsa.data.models.investor.Business
import com.technzone.miniborsa.data.models.investor.request.FavoriteRequest
import com.technzone.miniborsa.data.pref.favorite.FavoritePref
import com.technzone.miniborsa.data.repos.common.CommonRepo
import com.technzone.miniborsa.data.repos.investors.InvestorsRepo
import com.technzone.miniborsa.data.repos.user.UserRepo
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BusinessDetailsViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val investorsRepo: InvestorsRepo,
    private val commonRepo: CommonRepo,
    private var favoritePref: FavoritePref
) : BaseViewModel() {
    var businessToView: MutableLiveData<Business> = MutableLiveData(null)
    val isInvestor: MutableLiveData<Boolean> = MutableLiveData(getIsInvestor())
    fun getBusiness(
        id: Int
    ) = liveData {
        emit(APIResource.loading())
        val response =
            investorsRepo.getBusinessById(id)
        favoritePref.getFavoriteList().singleOrNull {it == response.data?.data?.id}?.let {
            response.data?.data?.isFavorite = true
        }
        emit(response)
    }
    fun getIsInvestor() =
        userRepo.getUser()?.roles?.singleOrNull { it.role == UserRoleEnums.INVESTOR_ROLE.value } != null
}