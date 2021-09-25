package com.technzone.miniborsa.ui.business.createbusiness.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.enums.BusinessTypeEnums
import com.technzone.miniborsa.data.enums.PropertyStatusEnums
import com.technzone.miniborsa.data.enums.UserRoleEnums
import com.technzone.miniborsa.data.models.business.business.OwnerBusiness
import com.technzone.miniborsa.data.models.business.businessrequest.BusinessRequest
import com.technzone.miniborsa.data.models.map.Address
import com.technzone.miniborsa.data.pref.user.UserPref
import com.technzone.miniborsa.data.repos.business.BusinessRepo
import com.technzone.miniborsa.data.repos.configuration.ConfigurationRepo
import com.technzone.miniborsa.data.repos.user.UserRepo
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import com.technzone.miniborsa.utils.extensions.createImageMultipart
import com.technzone.miniborsa.utils.pref.SharedPreferencesUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateBusinessViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val userPref: UserPref,
    private val sharedPreferencesUtil: SharedPreferencesUtil,
    private val configurationRepo: ConfigurationRepo,
    private val businessRepo: BusinessRepo
) : BaseViewModel() {

    var businessRequest: BusinessRequest = BusinessRequest()
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
    val propertyStatus: MutableLiveData<PropertyStatusEnums> =
        MutableLiveData(PropertyStatusEnums.FREEHOLD)
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

    fun buildBusinessRequest(): BusinessRequest {
        return BusinessRequest(
            websiteLink = webLink.value,
            counrty = "",
            city = "",
            englishDescription = summery.value,
            arabicDescription = summery.value,
            canRunfromHome = runFromHome.value,
            training = training.value,
            title = title.value,
            isRelocated = relocated.value,
            askingPriceBoth = leaseHoldAskingPrice.value,
            askingPriceNABoth = leaseHoldAskingPriceOnRequest.value,
            investmentPercentage = percentage.value,
            propertyStatus = propertyStatus.value?.value,
            annualTurnover = turnOver.value,
            annualTurnoverNA = turnoverOnRequest.value,
            listLocation = listLocation.value,
            establishedYear = date.value,
            categories = arrayListOf(),
            address = addressStr.value,
            isConfidential = confidential.value,
            askingPrice = freeHoldAskingPrice.value,
            askingPriceNA = freeholdAskingPriceOnRequest.value,
            countries = arrayListOf(),
            annualNetProfitNA = netProfitOnRequest.value,
            annualNetProfit = netProfit.value,
            businessType = businessType,
            fields = arrayListOf(),
            properties = arrayListOf()
        )
    }

    fun buildBusinessRequestFromBusiness() {
        businessRequest.let {
            webLink.value = it.websiteLink
//            counrty = "",
//            city = "",
            summery.value = it.englishDescription
            runFromHome.value = it.canRunfromHome
            training.value = it.training
            title.value = it.title
            relocated.value = it.isRelocated
            leaseHoldAskingPrice.value = it.askingPriceBoth
            leaseHoldAskingPriceOnRequest.value = it.askingPriceNABoth
            percentage.value = it.investmentPercentage
            propertyStatus.value = PropertyStatusEnums.getStatusByValue(
                it.propertyStatus ?: PropertyStatusEnums.FREEHOLD.value
            )
            turnOver.value = it.annualTurnover
            turnoverOnRequest.value = it.annualTurnoverNA
            listLocation.value = it.listLocation
            date.value = it.establishedYear
//            categories = arrayListOf(),
            addressStr.value = it.address
            confidential.value = it.isConfidential
            freeHoldAskingPrice.value = it.askingPrice
            freeholdAskingPriceOnRequest.value = it.askingPriceNA
//            countries = arrayListOf(),
            netProfit.value = it.annualNetProfit
            netProfitOnRequest.value = it.annualNetProfitNA
            businessType = it.businessType ?: 0
//            fields = arrayListOf(),
//            properties = arrayListOf()
        }
    }

    fun getCategories() = liveData {
        emit(APIResource.loading())
        val response =
            configurationRepo.getCategories(
                parentId = 0,
                pageSize = 1000,
                pageNumber = 1
            )
        emit(response)
    }

    fun getProperties() = liveData {
        emit(APIResource.loading())
        val response =
            configurationRepo.getProperties(
                parentId = 0,
                pageSize = 1000,
                pageNumber = 1
            )
        emit(response)
    }

    fun getOwnerBusiness(): OwnerBusiness {
        return OwnerBusiness()
    }

    fun requestBusiness() = liveData {
        emit(APIResource.loading())
        val response =
            if (isUserHasBusinessRoles())
            businessRepo.requestBusiness(businessRequest)
        else businessRepo.requestCompany(businessRequest)
        emit(response)
    }

    fun addFile(file: String) = liveData {
        emit(APIResource.loading())
        val response =
            if (isUserHasBusinessRoles())
                businessRepo.addBusinessRequestFiles(
                    businessRequest.businessId,
                    arrayListOf(file.createImageMultipart("Files"))
                )
            else businessRepo.addBusinessRequestFiles(
                businessRequest.businessId,
                arrayListOf(file.createImageMultipart("Files"))
            )
        emit(response)
    }

    fun deleteFile(fileId: Int) = liveData {
        emit(APIResource.loading())
        val response =
            if (isUserHasBusinessRoles())
                businessRepo.deleteBusinessRequestFiles(fileId)
            else businessRepo.deleteCompanyRequestFiles(fileId)
        emit(response)
    }

    fun addImage(file: String) = liveData {
        emit(APIResource.loading())
        val response =
            if (isUserHasBusinessRoles())
                businessRepo.addBusinessRequestImage(
                    businessRequest.businessId,
                    arrayListOf(file.createImageMultipart("Files"))
                )
            else businessRepo.addCompanyRequestImage(
                businessRequest.businessId,
                arrayListOf(file.createImageMultipart("Files"))
            )
        emit(response)
    }

    fun deleteImage(fileId: Int) = liveData {
        emit(APIResource.loading())
        val response =
            if (isUserHasBusinessRoles())
                businessRepo.deleteBusinessRequestImage(fileId)
            else businessRepo.deleteCompanyRequestImage(fileId)
        emit(response)
    }

    fun isUserHasBusinessRoles(): Boolean {
        return userRepo.getUser()?.roles?.singleOrNull { it.role == UserRoleEnums.BUSINESS_ROLE.value } != null
    }
}