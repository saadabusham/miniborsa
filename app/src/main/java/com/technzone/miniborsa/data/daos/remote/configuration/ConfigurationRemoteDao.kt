package com.technzone.miniborsa.data.daos.remote.configuration

import com.technzone.miniborsa.data.api.response.ResponseWrapper
import com.technzone.miniborsa.data.models.configuration.ConfigurationWrapperResponse
import com.technzone.miniborsa.data.models.country.Country
import retrofit2.http.GET

interface ConfigurationRemoteDao {

    @GET("api/configuration")
    suspend fun getAppConfiguration(): ResponseWrapper<ConfigurationWrapperResponse>

    @GET("api/owner/company/country")
    suspend fun getCountries(): ResponseWrapper<List<Country>>
}