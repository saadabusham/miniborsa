package com.technzone.miniborsa.ui.investor.recentviewed.viewmodels

import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.enums.UserEnums
import com.technzone.miniborsa.data.models.investor.Business
import com.technzone.miniborsa.data.repos.searchbusiness.SearchedBusinessRepo
import com.technzone.miniborsa.data.repos.user.UserRepo
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentViewViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val searchedBusinessRepo: SearchedBusinessRepo
) : BaseViewModel() {

    fun saveSearchBusinesses(list: List<Business>) = viewModelScope.launch {
        searchedBusinessRepo.saveBusinesses(list)
    }

    fun saveSearchBusiness(business: Business) = viewModelScope.launch {
        searchedBusinessRepo.saveBusiness(business)
    }

    fun getSearchedBusiness() = liveData {
        emit(searchedBusinessRepo.getBusiness())
    }

    fun isUserLoggedIn() = userRepo.getUserStatus() == UserEnums.UserState.LoggedIn
}