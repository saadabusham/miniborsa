package com.technzone.minibursa.ui.investor.recentviewed.viewmodels

import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.technzone.minibursa.data.enums.UserEnums
import com.technzone.minibursa.data.models.investor.Business
import com.technzone.minibursa.data.pref.favorite.FavoritePref
import com.technzone.minibursa.data.repos.searchbusiness.SearchedBusinessRepo
import com.technzone.minibursa.data.repos.user.UserRepo
import com.technzone.minibursa.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentViewViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val searchedBusinessRepo: SearchedBusinessRepo,
    private var favoritePref: FavoritePref
) : BaseViewModel() {

    fun saveSearchBusinesses(list: List<Business>) = viewModelScope.launch {
        searchedBusinessRepo.saveBusinesses(list)
    }

    fun saveSearchBusiness(business: Business) = viewModelScope.launch {
        searchedBusinessRepo.saveBusiness(business)
    }

    fun getSearchedBusiness() = liveData {
        emit(searchedBusinessRepo.getBusiness().apply {
            favoritePref.getFavoriteList().forEach { favId ->
                singleOrNull { it.id == favId }?.let { it.isFavorite = true }
            }
        })
    }

    fun isUserLoggedIn() = userRepo.getUserStatus() == UserEnums.UserState.LoggedIn
}