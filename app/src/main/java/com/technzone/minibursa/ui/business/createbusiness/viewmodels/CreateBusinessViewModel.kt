package com.technzone.minibursa.ui.business.createbusiness.viewmodels

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.technzone.minibursa.data.api.response.APIResource
import com.technzone.minibursa.data.common.Constants.DEFAULT_MAX_VALUE
import com.technzone.minibursa.data.common.Constants.DEFAULT_MIN_VALUE
import com.technzone.minibursa.data.common.Constants.MAX_PRICE_LENGTH
import com.technzone.minibursa.data.enums.BusinessTypeEnums
import com.technzone.minibursa.data.enums.PropertyStatusEnums
import com.technzone.minibursa.data.models.Media
import com.technzone.minibursa.data.models.business.business.OwnerBusiness
import com.technzone.minibursa.data.models.business.businessrequest.BusinessRequest
import com.technzone.minibursa.data.models.general.GeneralLookup
import com.technzone.minibursa.data.models.investor.FieldsItem
import com.technzone.minibursa.data.models.map.Address
import com.technzone.minibursa.data.pref.user.UserPref
import com.technzone.minibursa.data.repos.business.BusinessRepo
import com.technzone.minibursa.data.repos.configuration.ConfigurationRepo
import com.technzone.minibursa.data.repos.user.UserRepo
import com.technzone.minibursa.ui.base.bindingadapters.getLoadingUrl
import com.technzone.minibursa.ui.base.viewmodel.BaseViewModel
import com.technzone.minibursa.utils.extensions.createFileMultipart
import com.technzone.minibursa.utils.extensions.createImageMultipart
import com.technzone.minibursa.utils.extensions.getCurrentYear
import com.technzone.minibursa.utils.pref.SharedPreferencesUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CreateBusinessViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val configurationRepo: ConfigurationRepo,
    private val businessRepo: BusinessRepo
) : BaseViewModel() {
    var businessId: Int? = null
    val defaultMinValue: Int = DEFAULT_MIN_VALUE
    val defaultMaxValue: Int = DEFAULT_MAX_VALUE
    val maxLength: Int = MAX_PRICE_LENGTH
    var businessType: Int = BusinessTypeEnums.BUSINESS_FOR_SALE.value
    var hasBusiness: Boolean = false
    var companyDraft: Boolean = false
    var businessDraft: Boolean = false
    val percentage: MutableLiveData<Int> = MutableLiveData(0)
    val title: MutableLiveData<String> = MutableLiveData("")
    val summery: MutableLiveData<String> = MutableLiveData("")
    val addressStr: MutableLiveData<String> = MutableLiveData("")
    val country: MutableLiveData<String> = MutableLiveData("")
    val city: MutableLiveData<String> = MutableLiveData("")
    val address: MutableLiveData<Address> = MutableLiveData()
    val date: MutableLiveData<String> = MutableLiveData("")
    val isNegotiable: MutableLiveData<Boolean> = MutableLiveData()
    val propertyStatus: MutableLiveData<PropertyStatusEnums> =
        MutableLiveData(PropertyStatusEnums.FREEHOLD)
    val videoUrl: MutableLiveData<String> = MutableLiveData("")
    val webLink: MutableLiveData<String> = MutableLiveData("")
    val training: MutableLiveData<String> = MutableLiveData("")

    val freeholdAskingPriceOnRequest: MutableLiveData<Boolean> = MutableLiveData(false)
    val leaseHoldAskingPriceOnRequest: MutableLiveData<Boolean> = MutableLiveData(false)
    val netProfitOnRequest: MutableLiveData<Boolean> = MutableLiveData(false)
    val turnoverOnRequest: MutableLiveData<Boolean> = MutableLiveData(false)
    val propertyStatusNa: MutableLiveData<Boolean> = MutableLiveData(false)

    val freeHoldAskingPrice: MutableLiveData<Double> = MutableLiveData()
    val leaseHoldAskingPrice: MutableLiveData<Double> =
        MutableLiveData()
    val netProfit: MutableLiveData<Double> = MutableLiveData()
    val turnOver: MutableLiveData<Double> = MutableLiveData()
    val sharePercentage: MutableLiveData<Int> = MutableLiveData(100)
    val selectedItemsCount: MutableLiveData<Int> = MutableLiveData(0)

    val listLocation: MutableLiveData<Boolean> = MutableLiveData(false)
    val relocated: MutableLiveData<Boolean> = MutableLiveData(false)
    val runFromHome: MutableLiveData<Boolean> = MutableLiveData(false)
    val confidential: MutableLiveData<Boolean> = MutableLiveData(false)

    var categories: MutableList<Int> = mutableListOf()
    var countries: MutableList<GeneralLookup> = mutableListOf()
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
            id = businessId,
            websiteLink = webLink.value,
            counrty = country.value,
            city = city.value,
            englishDescription = summery.value,
            arabicDescription = summery.value,
            canRunfromHome = runFromHome.value,
            training = training.value,
            title = title.value,
            isRelocated = relocated.value,
            askingPriceBoth = leaseHoldAskingPrice.value,
            askingPriceNABoth = leaseHoldAskingPriceOnRequest.value,
            investmentPercentage = percentage.value,
            propertyStatus = if(propertyStatusNa.value == true) PropertyStatusEnums.NA.value else propertyStatus.value?.value,
            annualTurnover = turnOver.value,
            annualTurnoverNA = turnoverOnRequest.value,
            listLocation = listLocation.value,
            establishedYear = date.value,
            categories = categories,
            address = addressStr.value,
            isConfidential = confidential.value,
            askingPrice = freeHoldAskingPrice.value,
            askingPriceNA = freeholdAskingPriceOnRequest.value,
            countries = countries.map { it.id },
            annualNetProfitNA = netProfitOnRequest.value,
            annualNetProfit = netProfit.value,
            businessType = businessType,
            fields = fields,
            properties = properties,
            videoLink = videoUrl.value,
            isNegotiable = isNegotiable.value,
            images = images,
            files = files
        )
    }

    fun buildBusinessRequestFromBusiness(business: OwnerBusiness) {
        business.let {
            businessId = it.id
            webLink.postValue(it.websiteLink)
            country.postValue(it.counrty)
            city.postValue(it.city)
            summery.postValue(it.englishDescription)
            runFromHome.postValue(it.canRunfromHome)
            training.postValue(it.training)
            title.postValue(it.title)
            relocated.postValue(it.isRelocated)
            leaseHoldAskingPrice.postValue(it.askingPriceBoth)
            leaseHoldAskingPriceOnRequest.postValue(it.askingPriceNABoth)
            percentage.postValue(it.investmentPercentage)
            propertyStatusNa.postValue(it.propertyStatusNa)
            propertyStatus.value = (PropertyStatusEnums.getStatusByValue(
                it.propertyStatus
            ))
            propertyStatusNa.postValue(propertyStatus.value == PropertyStatusEnums.NA)
            turnOver.postValue(it.annualTurnover)
            turnoverOnRequest.postValue(it.annualTurnoverNA)
            listLocation.postValue(it.listLocation)
            date.postValue(it.establishedYear ?: getCurrentYear())
            categories = it.categories?.map { it.id ?: 0 }?.toMutableList() ?: mutableListOf()
            addressStr.postValue(it.address)
            confidential.postValue(it.isConfidential)
            freeHoldAskingPrice.postValue(it.askingPrice)
            freeholdAskingPriceOnRequest.postValue(it.askingPriceNA)
            countries = it.countries?.toMutableList() ?: mutableListOf()
            netProfit.postValue(it.annualNetProfit)
            netProfitOnRequest.postValue(it.annualNetProfitNA)
            businessType = it.businessType ?: 0
            fields = it.fields?.toMutableList() ?: mutableListOf()
            properties = it.properties?.map { it.id }?.toMutableList() ?: mutableListOf()
            images = (it.images?.toMutableList() ?: mutableListOf()).apply {
                it.icon?.let {
                    add(0, Media(name = it, id = -1))
                }
            }
            files = it.files?.toMutableList() ?: mutableListOf()
            isNegotiable.postValue(it.isNegotiable)
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

    fun addFile(file: String) = liveData {
        emit(APIResource.loading())
        val response =
            if (isHasBusiness())
                businessRepo.addBusinessRequestFiles(
                    businessId,
                    arrayListOf(file.createFileMultipart("Files"))
                )
            else businessRepo.addCompanyRequestFiles(
                businessId,
                arrayListOf(file.createFileMultipart("Files"))
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

    fun addIconImage(file: String) = liveData {
        emit(APIResource.loading())
        val response =
            if (isHasBusiness())
                businessRepo.addBusinessIcon(
                    businessId,
                    file.createImageMultipart("Icon")
                )
            else businessRepo.addCompanyIcon(
                businessId,
                file.createImageMultipart("Icon")
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

    fun deleteCompanyIcon() = liveData {
        emit(APIResource.loading())
        val response =
            businessRepo.deleteCompanyIcon()
        emit(response)
    }

    fun requestCompany(name: String) = liveData {
        emit(APIResource.loading())
        val response = businessRepo.requestCompany(name)
        emit(response)
    }

    fun isHasBusiness(): Boolean {
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
            businessRepo.getRequestBusiness(businessId ?: 0)
        emit(response)
    }

    fun getCountries() = liveData {
        emit(APIResource.loading())
        val response = configurationRepo.getCountries(
            pageSize = 1000,
            pageNumber = 1
        )
        emit(response)
    }

    fun reUploadBusinessImageAndFiles() = liveData {
        emit(APIResource.loading())
        images.withIndex().forEach {
            loadImagesAndUpload(it.value.name ?: "", it.index == 0)
        }
        files.withIndex().forEach {
            loadFileAndUpload(it.value.name ?: "")
        }
        emit(APIResource.success(null, messages = null, statusCode = 0))
    }

    private suspend fun loadImagesAndUpload(image: String, mainImage: Boolean) {
        Glide.with(context)
            .asFile()
            .load(getLoadingUrl(image ?: "").toString())
            .into(object : CustomTarget<File>() {
                override fun onLoadCleared(placeholder: Drawable?) {

                }

                override fun onResourceReady(
                    resource: File,
                    transition: Transition<in File>?
                ) {
                    viewModelScope.launch {
                        if (mainImage) {
                            reUploadIconImage(resource.path)
                        } else
                            reUploadImage(resource.path)
                    }
                }

            })
    }

    private suspend fun loadFileAndUpload(file: String) {
        Glide.with(context)
            .asFile()
            .load(getLoadingUrl(file ?: "").toString())
            .into(object : CustomTarget<File>() {
                override fun onLoadCleared(placeholder: Drawable?) {

                }

                override fun onResourceReady(
                    resource: File,
                    transition: Transition<in File>?
                ) {
                    viewModelScope.launch {
                        reUploadFiles(resource.path)
                    }
                }
            })
    }

    suspend fun reUploadIconImage(file: String) = viewModelScope.launch {
        if (isHasBusiness())
            businessRepo.addBusinessIcon(
                businessId,
                file.createImageMultipart("Icon")
            )
        else businessRepo.addCompanyIcon(
            businessId,
            file.createImageMultipart("Icon")
        )
    }

    suspend fun reUploadImage(file: String) = viewModelScope.launch {
        if (isHasBusiness())
            businessRepo.addBusinessRequestImage(
                businessId,
                arrayListOf(file.createImageMultipart("Files"))
            )
        else businessRepo.addCompanyRequestImage(
            businessId,
            arrayListOf(file.createImageMultipart("Files"))
        )
    }

    suspend fun reUploadFiles(file: String) = viewModelScope.launch {
        if (isHasBusiness())
            businessRepo.addBusinessRequestFiles(
                businessId,
                arrayListOf(file.createFileMultipart("Files"))
            )
        else businessRepo.addCompanyRequestFiles(
            businessId,
            arrayListOf(file.createFileMultipart("Files"))
        )
    }
}