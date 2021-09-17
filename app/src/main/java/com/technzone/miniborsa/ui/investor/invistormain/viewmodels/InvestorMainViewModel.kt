package com.technzone.miniborsa.ui.investor.invistormain.viewmodels

import androidx.lifecycle.liveData
import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.common.Constants
import com.technzone.miniborsa.data.repos.investors.InvestorsRepo
import com.technzone.miniborsa.data.repos.user.UserRepo
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InvestorMainViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val investorsRepo: InvestorsRepo
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

    fun getFavorites(
        pageNumber: Int
    ) = liveData {
        emit(APIResource.loading())
        val response =
            investorsRepo.getFavorites(pageNumber = pageNumber, pageSize = Constants.PAGE_SIZE)
        emit(response)
    }
}