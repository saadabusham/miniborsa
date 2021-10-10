package com.technzone.minibursa.ui.investor.invistormain.viewmodels

import androidx.lifecycle.liveData
import com.technzone.minibursa.data.api.response.APIResource
import com.technzone.minibursa.data.common.Constants
import com.technzone.minibursa.data.enums.NewsSectionEnums
import com.technzone.minibursa.data.enums.UserEnums
import com.technzone.minibursa.data.enums.UserRoleEnums
import com.technzone.minibursa.data.pref.favorite.FavoritePref
import com.technzone.minibursa.data.repos.business.BusinessRepo
import com.technzone.minibursa.data.repos.common.CommonRepo
import com.technzone.minibursa.data.repos.investors.InvestorsRepo
import com.technzone.minibursa.data.repos.user.UserRepo
import com.technzone.minibursa.ui.base.viewmodel.BaseViewModel
import com.technzone.minibursa.utils.pref.SharedPreferencesUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InvestorMainViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val investorsRepo: InvestorsRepo,
    private val commonRepo: CommonRepo,
    private val sharedPreferencesUtil: SharedPreferencesUtil,
    private val favoritePref: FavoritePref,
    private val businessRepo: BusinessRepo
) : BaseViewModel() {

    fun getBusiness(
        type: Int
    ) = liveData {
        emit(APIResource.loading())
        val response =
            investorsRepo.getBusinessByType(
                businessType = type,
                pageNumber = 1,
                pageSize = Constants.PAGE_SIZE
            )
        favoritePref.getFavoriteList().forEach { favId ->
            response.data?.data?.data?.singleOrNull { it.id == favId }?.let { it.isFavorite = true }
        }
        emit(response)
    }

    fun getBlogs(
    ) = liveData {
        emit(APIResource.loading())
        val response =
            commonRepo.getBlogs(
                pageNumber = 1,
                pageSize = Constants.PAGE_SIZE,
                section = NewsSectionEnums.ALL.value,
                isPinned = true
            )
        emit(response)
    }

    fun getPendingListing() = liveData {
        emit(APIResource.loading())
        val response =
            businessRepo.getRequestCompany()
        emit(response)
    }

    fun isUserLoggedIn() = userRepo.getUserStatus() == UserEnums.UserState.LoggedIn

    fun isFirstLogin() = userRepo.getIsFirstLogin()

    fun isInvestor() =
        userRepo.getUser()?.roles?.singleOrNull { it.role == UserRoleEnums.INVESTOR_ROLE.value } != null

    fun isBusinessOwner() =
        userRepo.getUser()?.roles?.singleOrNull { it.role == UserRoleEnums.BUSINESS_ROLE.value } != null

    fun setIsFirstLogin(b: Boolean) {
        userRepo.setIsFirstLogin(b)
    }

    fun isUserHasBusinessRoles(): Boolean {
        return userRepo.getUser()?.roles?.singleOrNull { it.role == UserRoleEnums.BUSINESS_ROLE.value } != null
    }

    fun isUserHasInvestorRoles(): Boolean {
        return userRepo.getUser()?.roles?.singleOrNull { it.role == UserRoleEnums.INVESTOR_ROLE.value } != null
    }

    fun setCurrentUserRoles(role: String) {
        return userRepo.setCurrentRole(role)
    }

    fun isNewNotification():Boolean {
        return sharedPreferencesUtil.getIsNewNotifications()
    }
}