package com.technzone.miniborsa.ui.investor.invistormain.viewmodels

import androidx.lifecycle.liveData
import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.common.Constants
import com.technzone.miniborsa.data.enums.UserEnums
import com.technzone.miniborsa.data.enums.UserRoleEnums
import com.technzone.miniborsa.data.repos.common.CommonRepo
import com.technzone.miniborsa.data.repos.investors.InvestorsRepo
import com.technzone.miniborsa.data.repos.user.UserRepo
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InvestorMainViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val investorsRepo: InvestorsRepo,
    private val commonRepo: CommonRepo
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
        emit(response)
    }

    fun getBlogs(
    ) = liveData {
        emit(APIResource.loading())
        val response =
            commonRepo.getBlogs(
                pageNumber = 1,
                pageSize = Constants.PAGE_SIZE,
                banner = true
            )
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

    fun isUserLoggedIn() = userRepo.getUserStatus() == UserEnums.UserState.LoggedIn

    fun isFirstLogin() = userRepo.getIsFirstLogin()

    fun isInvestor() =
        userRepo.getUser()?.roles?.single { it.role == UserRoleEnums.INVESTOR_ROLE.value } != null

    fun isBusinessOwner() =
        userRepo.getUser()?.roles?.single { it.role == UserRoleEnums.BUSINESS_ROLE.value } != null

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
}