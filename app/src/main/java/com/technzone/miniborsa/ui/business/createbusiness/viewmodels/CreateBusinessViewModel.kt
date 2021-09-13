package com.technzone.miniborsa.ui.business.createbusiness.viewmodels

import androidx.lifecycle.MutableLiveData
import com.technzone.miniborsa.data.enums.BusinessTypeEnums
import com.technzone.miniborsa.data.enums.PropertyStatusEnums
import com.technzone.miniborsa.data.pref.user.UserPref
import com.technzone.miniborsa.data.repos.user.UserRepo
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import com.technzone.miniborsa.utils.pref.SharedPreferencesUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateBusinessViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val userPref: UserPref,
    private val sharedPreferencesUtil: SharedPreferencesUtil,
) : BaseViewModel() {

    var businessType: Int = BusinessTypeEnums.BUSINESS_FOR_SALE.value
    val percentage: MutableLiveData<Int> = MutableLiveData(0)
    val title: MutableLiveData<String> = MutableLiveData("")
    val summery: MutableLiveData<String> = MutableLiveData("")
    val addressStr: MutableLiveData<String> = MutableLiveData("")
    val date: MutableLiveData<String> = MutableLiveData()
    val IsNegotiable: MutableLiveData<Boolean> = MutableLiveData()
    val propertyStatus: MutableLiveData<PropertyStatusEnums> = MutableLiveData(PropertyStatusEnums.FREEHOLD)
    val videoUrl: MutableLiveData<String> = MutableLiveData("")


    val freeholdAskingPriceOnRequest: MutableLiveData<Boolean> = MutableLiveData(false)
    val leaseHoldAskingPriceOnRequest: MutableLiveData<Boolean> = MutableLiveData(false)
    val netProfitOnRequest: MutableLiveData<Boolean> = MutableLiveData(false)
    val turnoverOnRequest: MutableLiveData<Boolean> = MutableLiveData(false)

    val freeHoldAskingPrice: MutableLiveData<String> = MutableLiveData("1000")
    val leaseHoldAskingPrice: MutableLiveData<Int> = MutableLiveData(1000)
    val netProfit: MutableLiveData<Int> = MutableLiveData(1000)
    val turnOver: MutableLiveData<Int> = MutableLiveData(1000)
    val sharePercentage: MutableLiveData<Int> = MutableLiveData(1000)

    val freeHoldAskingPriceStr: MutableLiveData<String> = MutableLiveData("1000")
    val leaseHoldAskingPriceStr: MutableLiveData<String> = MutableLiveData("1000")
    val netProfitStr: MutableLiveData<String> = MutableLiveData("1000")
    val turnOverStr: MutableLiveData<String> = MutableLiveData("1000")
}