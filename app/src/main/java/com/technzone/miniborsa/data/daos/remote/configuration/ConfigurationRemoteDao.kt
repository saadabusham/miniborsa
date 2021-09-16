package com.technzone.miniborsa.data.daos.remote.configuration

import com.technzone.miniborsa.data.api.response.ResponseWrapper
import com.technzone.miniborsa.data.models.configuration.ConfigurationWrapperResponse
import com.technzone.miniborsa.data.models.country.Country
import com.technzone.miniborsa.data.models.general.ListWrapper
import com.technzone.miniborsa.data.models.investor.CategoriesRequest
import com.technzone.miniborsa.data.models.investor.investors.CategoriesItem
import retrofit2.http.*

interface ConfigurationRemoteDao {

    @GET("api/configuration")
    suspend fun getAppConfiguration(): ResponseWrapper<ConfigurationWrapperResponse>

    @GET("api/owner/company/country")
    suspend fun getCountries(): ResponseWrapper<List<Country>>

    @POST("api/category")
    suspend fun getCategories(
        @Body categoriesRequest: CategoriesRequest
    ): ResponseWrapper<ListWrapper<CategoriesItem>>
}