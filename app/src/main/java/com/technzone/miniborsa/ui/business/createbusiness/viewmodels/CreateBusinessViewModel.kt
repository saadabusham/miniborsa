package com.technzone.miniborsa.ui.business.createbusiness.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.enums.BusinessTypeEnums
import com.technzone.miniborsa.data.enums.PropertyStatusEnums
import com.technzone.miniborsa.data.models.Media
import com.technzone.miniborsa.data.models.business.business.OwnerBusiness
import com.technzone.miniborsa.data.models.business.businessrequest.BusinessRequest
import com.technzone.miniborsa.data.models.investor.FieldsItem
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
    var businessId: Int? = null
    val defaultMinValue: Int = 1000
    val defaultMaxValue: Int = 1000000
    var businessType: Int = BusinessTypeEnums.BUSINESS_FOR_SALE.value
    var hasBusiness: Boolean = false
    var companyDraft: Boolean = false
    var businessDraft: Boolean = false
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

    var categories: MutableList<Int> = mutableListOf()
    var countries: MutableList<Int> = mutableListOf()
    var fields: MutableList<FieldsItem> = mutableListOf()
    var properties: MutableList<Int?> = mutableListOf()
    var images: MutableList<Media> = mutableListOf()
    var files: MutableList<Media> = mutableListOf()

    fun getCategoriesCount(catNumber: Int): Int {
        return if (categories.size >= 3) catNumber else categories.size + 1
    }

    fun buildBusinessRequest(): BusinessRequest {
        return BusinessRequest(
            businessId = businessId,
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
            categories = categories,
            address = addressStr.value,
            isConfidential = confidential.value,
            askingPrice = freeHoldAskingPrice.value,
            askingPriceNA = freeholdAskingPriceOnRequest.value,
            countries = countries,
            annualNetProfitNA = netProfitOnRequest.value,
            annualNetProfit = netProfit.value,
            businessType = businessType,
            fields = fields,
            properties = properties,
            videoLink = videoUrl.value
        )
    }

    fun buildBusinessRequestFromBusiness(business: OwnerBusiness) {
        business.let {
            businessId = it.id
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
            categories = it.categories?.toMutableList() ?: mutableListOf()
            addressStr.value = it.address
            confidential.value = it.isConfidential
            freeHoldAskingPrice.value = it.askingPrice
            freeholdAskingPriceOnRequest.value = it.askingPriceNA
            countries = it.countries?.toMutableList() ?: mutableListOf()
            netProfit.value = it.annualNetProfit
            netProfitOnRequest.value = it.annualNetProfitNA
            businessType = it.businessType ?: 0
            fields = it.fields?.toMutableList() ?: mutableListOf()
            properties = it.properties?.map { it.id }?.toMutableList() ?: mutableListOf()
            images = it.images?.toMutableList() ?: mutableListOf()
            files = it.files?.toMutableList() ?: mutableListOf()
        }
    }

    fun getCategories() = liveData {
        emit(APIResource.loading())
        val response =
            configurationRepo.getCategories(
                parentId = null,
                pageSize = 1000,
                pageNumber = 1
            )
        emit(response)
    }

    fun getProperties() = liveData {
        emit(APIResource.loading())
        val response =
            configurationRepo.getProperties(
                parentId = null,
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
        val response = businessRepo.requestBusiness(buildBusinessRequest())
        emit(response)
    }

    fun updateRequestBusiness() = liveData {
        emit(APIResource.loading())
        val response =
            if (isHasBusiness())
                businessRepo.updateBusinessRequest(buildBusinessRequest())
            else businessRepo.updateCompanyRequest(buildBusinessRequest())
        emit(response)
    }

    fun sendRequestBusiness(businessId: Int) = liveData {
        emit(APIResource.loading())
        val response =
            if (isHasBusiness())
                businessRepo.sendBusinessRequest(businessId)
            else businessRepo.sendCompanyRequest(businessId)
        emit(response)
    }

    fun addFile(file: String) = liveData {
        emit(APIResource.loading())
        val response =
            if (isHasBusiness())
                businessRepo.addBusinessRequestFiles(
                    businessId,
                    arrayListOf(file.createImageMultipart("Files"))
                )
            else businessRepo.addCompanyRequestFiles(
                businessId,
                arrayListOf(file.createImageMultipart("Files"))
            )
        emit(response)
    }

    fun deleteFile(fileId: Int) = liveData {
        emit(APIResource.loading())
        val response =
            if (isHasBusiness())
                businessRepo.deleteBusinessRequestFiles(fileId)
            else businessRepo.deleteCompanyRequestFiles(fileId)
        emit(response)
    }

    fun addImage(file: String) = liveData {
        emit(APIResource.loading())
        val response =
            if (isHasBusiness())
                businessRepo.addBusinessRequestImage(
                    businessId,
                    arrayListOf(file.createImageMultipart("Files"))
                )
            else businessRepo.addCompanyRequestImage(
                businessId,
                arrayListOf(file.createImageMultipart("Files"))
            )
        emit(response)
    }

    fun deleteImage(fileId: Int) = liveData {
        emit(APIResource.loading())
        val response =
            if (isHasBusiness())
                businessRepo.deleteBusinessRequestImage(fileId)
            else businessRepo.deleteCompanyRequestImage(fileId)
        emit(response)
    }

    fun requestCompany(name: String) = liveData {
        emit(APIResource.loading())
        val response = businessRepo.requestCompany(name)
        emit(response)
    }

    private fun isHasBusiness(): Boolean {
        return hasBusiness && !companyDraft
    }

    fun getCompanyRequest() = liveData {
        emit(APIResource.loading())
        val response =
            businessRepo.getRequestCompany()
        emit(response)
    }
    fun getBusinessRequest() = liveData {
        emit(APIResource.loading())
        val response =
            businessRepo.getRequestBusiness(businessId?:0)
        emit(response)
    }
}