package com.technzone.miniborsa.ui.investor.invistormain.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.common.Constants
import com.technzone.miniborsa.data.enums.UserRoleEnums
import com.technzone.miniborsa.data.models.investor.Business
import com.technzone.miniborsa.data.models.investor.request.FavoriteRequest
import com.technzone.miniborsa.data.repos.common.CommonRepo
import com.technzone.miniborsa.data.repos.investors.InvestorsRepo
import com.technzone.miniborsa.data.repos.user.UserRepo
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val investorsRepo: InvestorsRepo,
    private val commonRepo: CommonRepo
) : BaseViewModel() {
    var businessToView: MutableLiveData<Business> = MutableLiveData(null)
    val isInvestor: MutableLiveData<Boolean> = MutableLiveData(getIsInvestor())
    fun getBusiness(
        id: Int
    ) = liveData {
        emit(APIResource.loading())
        val response =
            investorsRepo.getBusinessById(id)
        emit(response)
    }


    fun getFavorites(
        pageNumber: Int
    ) = liveData {
        emit(APIResource.loading())
        val response =
            investorsRepo.getFavorites(pageNumber = pageNumber, pageSize = Constants.PAGE_SIZE)
        emit(response)
    }
    fun addToWishList(
        businessId: Int
    ) = liveData {
        emit(APIResource.loading())
        val response =
            commonRepo.addFavorite(FavoriteRequest(businessId, userRepo.getUser()?.id))
        emit(response)
    }

    fun removeFromWishList(
        businessId: Int
    ) = liveData {
        emit(APIResource.loading())
        val response =
            commonRepo.removeFavorite(FavoriteRequest(businessId, userRepo.getUser()?.id))
        emit(response)
    }


    fun getIsInvestor() =
        userRepo.getUser()?.roles?.singleOrNull { it.role == UserRoleEnums.INVESTOR_ROLE.value } != null
}