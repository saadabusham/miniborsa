package com.technzone.minibursa.ui.business.investors.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.technzone.minibursa.data.api.response.APIResource
import com.technzone.minibursa.data.models.investor.investors.Investor
import com.technzone.minibursa.data.models.investor.investors.InvestorFilter
import com.technzone.minibursa.data.repos.business.BusinessRepo
import com.technzone.minibursa.data.repos.configuration.ConfigurationRepo
import com.technzone.minibursa.data.repos.investors.InvestorsRepo
import com.technzone.minibursa.data.repos.twilio.TwilioRepo
import com.technzone.minibursa.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InvestorsViewModel @Inject constructor(
    private val businessRepo: BusinessRepo,
    private val investorsRepo: InvestorsRepo,
    private val configurationRepo: ConfigurationRepo,
    private val twilioRepo: TwilioRepo
) : BaseViewModel() {

    var investorFilter: InvestorFilter = InvestorFilter()
    var investorToView: MutableLiveData<Investor>? = MutableLiveData()
    var investorId: Int? = null
    fun getInvestors() = liveData {
        emit(APIResource.loading())
        val response = investorsRepo.getInvestors(investorFilter)
        emit(response)
    }

    fun getInvestorById(id: Int) = liveData {
        emit(APIResource.loading())
        val response = investorsRepo.getInvestorById(id)
        emit(response)
    }

    fun getCountries() = liveData {
        emit(APIResource.loading())
        val response = configurationRepo.getCountries(
            pageSize = 1000,
            pageNumber = 1
        )
        emit(response)
    }


    fun getChanelId(
        investorId:String,
        businessId: Int
    ) = liveData {
        emit(APIResource.loading())
        val response =
            twilioRepo.getBusinessToInvestorChannelId(investorId,businessId)
        emit(response)
    }

    fun getListing() = liveData {
        emit(APIResource.loading())
        val response =
            businessRepo.getOwnerBusiness(pageSize = 1000, pageNumber = 1)
        emit(response)
    }

}