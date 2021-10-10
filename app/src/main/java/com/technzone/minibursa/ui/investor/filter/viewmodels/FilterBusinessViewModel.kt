package com.technzone.minibursa.ui.investor.filter.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.technzone.minibursa.data.api.response.APIResource
import com.technzone.minibursa.data.enums.GenderEnums
import com.technzone.minibursa.data.enums.UserEnums
import com.technzone.minibursa.data.models.investor.Business
import com.technzone.minibursa.data.models.map.Address
import com.technzone.minibursa.data.pref.favorite.FavoritePref
import com.technzone.minibursa.data.repos.configuration.ConfigurationRepo
import com.technzone.minibursa.data.repos.investors.InvestorsRepo
import com.technzone.minibursa.data.repos.searchbusiness.SearchedBusinessRepo
import com.technzone.minibursa.data.repos.user.UserRepo
import com.technzone.minibursa.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterBusinessViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val investorsRepo: InvestorsRepo,
    private val configurationRepo: ConfigurationRepo,
    private val searchedBusinessRepo: SearchedBusinessRepo,
    private val favoritePref: FavoritePref
) : BaseViewModel() {
    val defaultMinValue: Int = 1000
    val defaultMaxValue: Int = 1000000
    var selectedBusinessType: MutableLiveData<Int> = MutableLiveData()
    val searchText: MutableLiveData<String> = MutableLiveData()
    val addressStr: MutableLiveData<String> = MutableLiveData()
    val address: MutableLiveData<Address> = MutableLiveData()
    val filterActive: MutableLiveData<Boolean> = MutableLiveData(true)
    val maleSelected: MutableLiveData<Boolean> = MutableLiveData(true)
    val itemFoundCount: MutableLiveData<Int> = MutableLiveData(0)
    val min: MutableLiveData<Int> = MutableLiveData(0)
    val max: MutableLiveData<Int> = MutableLiveData(0)
    var categories:List<Int> = mutableListOf()
    var pageNumber: Int = 1
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
                businessType = selectedBusinessType.value,
                pageNumber = pageNumber,
                pageSize = 10,
                categories = if(categories.isNullOrEmpty()) null else categories,
                gender = if(maleSelected.value == true ) GenderEnums.MALE.ordinal else GenderEnums.FEMALE.ordinal,
                active = if(filterActive.value == null) null else filterActive.value,
                askingPriceRangeFrom = if(min.value == 0 ) null else min.value ,
                askingPriceRangeTo = if(max.value == 0) null else max.value,
                title = searchText.value
            )
        favoritePref.getFavoriteList().forEach { favId ->
            response.data?.data?.data?.singleOrNull { it.id == favId }?.let { it.isFavorite = true }
        }
        emit(response)
    }

    fun getCategories() = liveData {
        emit(APIResource.loading())
        val response =
            configurationRepo.getCategories(
                    parentId = null,
                    pageSize = 1000,
                    pageNumber = 1)
        emit(response)
    }

    fun isUserLoggedIn() = userRepo.getUserStatus() == UserEnums.UserState.LoggedIn

    fun saveSearchBusinesses(list:List<Business>)= viewModelScope.launch{
        searchedBusinessRepo.saveBusinesses(list)
    }
    fun saveSearchBusiness(business:Business)= viewModelScope.launch{
        searchedBusinessRepo.saveBusiness(business)
    }
}