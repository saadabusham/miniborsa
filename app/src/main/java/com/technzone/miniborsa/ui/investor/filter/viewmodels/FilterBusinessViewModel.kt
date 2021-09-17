package com.technzone.miniborsa.ui.investor.filter.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.models.investor.GeneralRequest
import com.technzone.miniborsa.data.repos.configuration.ConfigurationRepo
import com.technzone.miniborsa.data.repos.investors.InvestorsRepo
import com.technzone.miniborsa.data.repos.user.UserRepo
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterBusinessViewModel @Inject constructor(
    val userRepo: UserRepo,
    private val investorsRepo: InvestorsRepo,
    private val configurationRepo: ConfigurationRepo
) : BaseViewModel() {
    var selectedBusinessType: Int? = null
    val searchText: MutableLiveData<String> = MutableLiveData()
    val addressStr: MutableLiveData<String> = MutableLiveData()
    val filterActive: MutableLiveData<Boolean> = MutableLiveData(true)
    val maleSelected: MutableLiveData<Boolean> = MutableLiveData(true)
    val pageNumber: Int = 1
    fun onActiveClicked() {
        filterActive.value = true
    }

    fun onInActiveClicked() {
        filterActive.value = false
    }

    fun onMaleClicked() {
        maleSelected.value = true
    }

    fun onFeMaleClicked() {
        maleSelected.value = false
    }

    fun getBusiness(
    ) = liveData {
        emit(APIResource.loading())
        val response =
            investorsRepo.getBusinessByType(
                businessType = selectedBusinessType,
                pageNumber = pageNumber,
                pageSize = 10
            )
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

}