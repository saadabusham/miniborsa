package com.technzone.miniborsa.data.repos.configuration

import com.technzone.miniborsa.common.CommonEnums
import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.api.response.ResponseWrapper
import com.technzone.miniborsa.data.models.configuration.ConfigurationWrapperResponse
import com.technzone.miniborsa.data.models.country.Country
import com.technzone.miniborsa.data.models.general.ListWrapper
import com.technzone.miniborsa.data.models.investor.CategoriesRequest
import com.technzone.miniborsa.data.models.investor.investors.CategoriesItem
import retrofit2.http.Field

interface ConfigurationRepo {

    fun setAppLanguage(selectedLanguage: CommonEnums.Languages)
    fun getAppLanguage(): CommonEnums.Languages

    suspend fun loadConfigurationData(): APIResource<ResponseWrapper<ConfigurationWrapperResponse>>

    suspend fun getCountries(): APIResource<ResponseWrapper<List<Country>>>

    suspend fun getCategories(
        categoriesRequest: CategoriesRequest
    )
    : APIResource<ResponseWrapper<ListWrapper<CategoriesItem>>>
}