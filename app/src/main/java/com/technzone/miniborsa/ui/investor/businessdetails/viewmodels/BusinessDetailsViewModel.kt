package com.technzone.miniborsa.ui.investor.businessdetails.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.enums.UserRoleEnums
import com.technzone.miniborsa.data.models.investor.Business
import com.technzone.miniborsa.data.repos.investors.InvestorsRepo
import com.technzone.miniborsa.data.repos.user.UserRepo
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BusinessDetailsViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val investorsRepo: InvestorsRepo
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

    fun getIsInvestor() =
        userRepo.getUser()?.roles?.singleOrNull { it.role == UserRoleEnums.INVESTOR_ROLE.value } != null
}