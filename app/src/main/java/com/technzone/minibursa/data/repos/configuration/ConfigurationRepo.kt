package com.technzone.minibursa.data.repos.configuration

import com.technzone.minibursa.common.CommonEnums
import com.technzone.minibursa.data.api.response.APIResource
import com.technzone.minibursa.data.api.response.ResponseWrapper
import com.technzone.minibursa.data.models.business.business.PropertiesItem
import com.technzone.minibursa.data.models.configuration.ConfigurationWrapperResponse
import com.technzone.minibursa.data.models.country.Country
import com.technzone.minibursa.data.models.general.ListWrapper
import com.technzone.minibursa.data.models.investor.investors.CategoriesItem

interface ConfigurationRepo {

    fun setAppLanguage(selectedLanguage: CommonEnums.Languages)
    fun getAppLanguage(): CommonEnums.Languages

    suspend fun loadConfigurationData(): APIResource<ResponseWrapper<ConfigurationWrapperResponse>>

    suspend fun getCountries(
        name: String?="",
        pageSize: Int,
        pageNumber: Int
    ): APIResource<ResponseWrapper<ListWrapper<Country>>>

    suspend fun getCategories(
        parentId: Int?=null,
        name: String? = "",
        pageSize: Int,
        pageNumber: Int
    ): APIResource<ResponseWrapper<ListWrapper<CategoriesItem>>>

    suspend fun getInvestorCategories(
        parentId: Int?=null,
        name: String? = "",
        pageSize: Int,
        pageNumber: Int
    ): APIResource<ResponseWrapper<ListWrapper<CategoriesItem>>>

    suspend fun getProperties(
        parentId: Int? = null,
        name: String? = "",
        pageSize: Int,
        pageNumber: Int
    ): APIResource<ResponseWrapper<ListWrapper<PropertiesItem>>>
}