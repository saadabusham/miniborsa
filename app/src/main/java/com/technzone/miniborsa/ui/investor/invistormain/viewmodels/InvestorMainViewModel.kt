package com.technzone.miniborsa.ui.investor.invistormain.viewmodels

import androidx.lifecycle.MutableLiveData
import com.technzone.miniborsa.data.repos.auth.UserRepo
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InvestorMainViewModel @Inject constructor(
    val userRepo: UserRepo
) : BaseViewModel() {
    val searchText: MutableLiveData<String> = MutableLiveData()
    val addressStr: MutableLiveData<String> = MutableLiveData()
    val filterActive: MutableLiveData<Boolean> = MutableLiveData(true)
    val maleSelected: MutableLiveData<Boolean> = MutableLiveData(true)

    fun onActiveClicked(){
        filterActive.value = true
    }
    fun onInActiveClicked(){
        filterActive.value = false
    }
    fun onMaleClicked(){
        maleSelected.value = true
    }
    fun onFeMaleClicked(){
        maleSelected.value = false
    }
}