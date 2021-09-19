package com.technzone.miniborsa.ui.business.investors.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.models.investor.investors.Investor
import com.technzone.miniborsa.data.models.investor.investors.InvestorFilter
import com.technzone.miniborsa.data.repos.business.BusinessRepo
import com.technzone.miniborsa.data.repos.investors.InvestorsRepo
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InvestorsViewModel @Inject constructor(
    private val businessRepo: BusinessRepo,
    private val investorsRepo: InvestorsRepo,
) : BaseViewModel() {

    var investorFilter: InvestorFilter = InvestorFilter()
    var investorToView: MutableLiveData<Investor>? = MutableLiveData()

    fun getInvestors() = liveData {
        emit(APIResource.loading())
        val response = investorsRepo.getInvestors(investorFilter)
        emit(response)
    }

    fun getInvestorById(id:Int) = liveData {
        emit(APIResource.loading())
        val response = investorsRepo.getInvestorById(id)
        emit(response)
    }
}