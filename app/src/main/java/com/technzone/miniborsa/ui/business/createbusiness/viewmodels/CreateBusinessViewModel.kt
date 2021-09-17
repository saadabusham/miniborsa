package com.technzone.miniborsa.ui.business.createbusiness.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.enums.BusinessTypeEnums
import com.technzone.miniborsa.data.enums.PropertyStatusEnums
import com.technzone.miniborsa.data.models.business.business.OwnerBusiness
import com.technzone.miniborsa.data.models.investor.GeneralRequest
import com.technzone.miniborsa.data.models.map.Address
import com.technzone.miniborsa.data.pref.user.UserPref
import com.technzone.miniborsa.data.repos.configuration.ConfigurationRepo
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
    private val configurationRepo: ConfigurationRepo
) : BaseViewModel() {

    val defaultMinValue: Int = 1000
    val defaultMaxValue: Int = 1000000
    var businessType: Int = BusinessTypeEnums.BUSINESS_FOR_SALE.value
    val percentage: MutableLiveData<Int> = MutableLiveData(0)
    val title: MutableLiveData<String> = MutableLiveData("")
    val summery: MutableLiveData<String> = MutableLiveData("")
    val addressStr: MutableLiveData<String> = MutableLiveData("")
    val address: MutableLiveData<Address> = MutableLiveData()
    val date: MutableLiveData<String> = MutableLiveData()
    val IsFreeHoldNegotiable: MutableLiveData<Boolean> = MutableLiveData()
    val IsLeaseHoldNegotiable: MutableLiveData<Boolean> = MutableLiveData()
    val propertyStatus: MutableLiveData<PropertyStatusEnums> = MutableLiveData(PropertyStatusEnums.FREEHOLD)
    val videoUrl: MutableLiveData<String> = MutableLiveData("")
    val webLink: MutableLiveData<String> = MutableLiveData("")
    val training: MutableLiveData<String> = MutableLiveData("")


    val freeholdAskingPriceOnRequest: MutableLiveData<Boolean> = MutableLiveData(false)
    val leaseHoldAskingPriceOnRequest: MutableLiveData<Boolean> = MutableLiveData(false)
    val netProfitOnRequest: MutableLiveData<Boolean> = MutableLiveData(false)
    val turnoverOnRequest: MutableLiveData<Boolean> = MutableLiveData(false)

    val freeHoldAskingPrice: MutableLiveData<Int> = MutableLiveData(1000)
    val leaseHoldAskingPrice: MutableLiveData<Int> = MutableLiveData(1000)
    val netProfit: MutableLiveData<Int> = MutableLiveData(1000)
    val turnOver: MutableLiveData<Int> = MutableLiveData(1000)
    val sharePercentage: MutableLiveData<Int> = MutableLiveData(1000)
    val selectedItemsCount: MutableLiveData<Int> = MutableLiveData(0)


    val listLocation: MutableLiveData<Boolean> = MutableLiveData(false)
    val relocated: MutableLiveData<Boolean> = MutableLiveData(false)
    val runFromHome: MutableLiveData<Boolean> = MutableLiveData(false)
    val confidential: MutableLiveData<Boolean> = MutableLiveData(false)


    fun getCategories() = liveData {
        emit(APIResource.loading())
        val response =
            configurationRepo.getCategories(
                    parentId = 0,
                    pageSize = 1000,
                    pageNumber = 1)
        emit(response)
    }

    fun getProperties() = liveData {
        emit(APIResource.loading())
        val response =
            configurationRepo.getProperties(
                    parentId = 0,
                    pageSize = 1000,
                    pageNumber = 1)
        emit(response)
    }

    fun getOwnerBusiness(): OwnerBusiness {
        return OwnerBusiness()
    }

}