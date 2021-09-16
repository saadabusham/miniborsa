package com.technzone.miniborsa.ui.business.investors.viewmodels

import androidx.lifecycle.liveData
import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.repos.business.BusinessRepo
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InvestorsViewModel @Inject constructor(
    private val businessRepo: BusinessRepo
) : BaseViewModel() {

    fun getInvestors() = liveData {
        emit(APIResource.loading())
        val response = businessRepo.getInvestors()
        emit(response)
    }
}