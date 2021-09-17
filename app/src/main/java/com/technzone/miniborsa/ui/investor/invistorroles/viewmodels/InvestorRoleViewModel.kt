package com.technzone.miniborsa.ui.investor.invistorroles.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.models.investor.GeneralRequest
import com.technzone.miniborsa.data.repos.configuration.ConfigurationRepo
import com.technzone.miniborsa.data.repos.investors.InvestorsRepo
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InvestorRoleViewModel @Inject constructor(
    private val configurationRepo: ConfigurationRepo,
    private val investorsRepo: InvestorsRepo
) : BaseViewModel() {
    val defaultMinValue: Int = 1000
    val defaultMaxValue: Int = 1000000
    val budgetOnRequest: MutableLiveData<Boolean> = MutableLiveData(false)
    val investmentPrice: MutableLiveData<Int> = MutableLiveData(1000)

    fun getCountries() = liveData {
        emit(APIResource.loading())
        val response = configurationRepo.getCountries()
        emit(response)
    }

    fun getCategories() = liveData {
        emit(APIResource.loading())
        val response =
            configurationRepo.getCategories(
                    parentId = 0,
                    pageSize = 1000,
                    pageNumber = 1)
        emit(response)
    }

    fun becomeInvestor(
        countries: List<Int>,
        categories: List<Int>
    ) = liveData {
        emit(APIResource.loading())
        val response =
            investorsRepo.becomeInvestor(
                null,
                investmentPrice.value?.toDouble() ?: 0.0,
                budgetOnRequest.value?:false,
                countries,
                categories
            )
        emit(response)
    }
}