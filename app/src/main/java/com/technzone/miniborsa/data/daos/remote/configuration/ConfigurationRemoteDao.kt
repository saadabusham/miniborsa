package com.technzone.miniborsa.data.daos.remote.configuration

import com.technzone.miniborsa.data.api.response.ResponseWrapper
import com.technzone.miniborsa.data.models.configuration.ConfigurationWrapperResponse
import com.technzone.miniborsa.data.models.country.Country
import com.technzone.miniborsa.data.models.general.ListWrapper
import com.technzone.miniborsa.data.models.investor.PropertiesItem
import com.technzone.miniborsa.data.models.investor.investors.CategoriesItem
import retrofit2.http.*

interface ConfigurationRemoteDao {

    @GET("api/configuration")
    suspend fun getAppConfiguration(): ResponseWrapper<ConfigurationWrapperResponse>

    @GET("api/country")
    suspend fun getCountries(
        @Query("Name") name:String?,
        @Query("PageSize") pageSize: Int,
        @Query("PageNumber") pageNumber: Int
    ): ResponseWrapper<ListWrapper<Country>>

    @GET("api/category")
    suspend fun getCategories(
        @Query("parentId") parentId:Int?,
        @Query("Name") name:String?,
        @Query("PageSize") pageSize: Int,
        @Query("PageNumber") pageNumber: Int
    ): ResponseWrapper<ListWrapper<CategoriesItem>>

    @GET("api/Property")
    suspend fun getProperty(
        @Query("parentId") parentId:Int,
        @Query("Name") name:String?,
        @Query("PageSize") pageSize: Int,
        @Query("PageNumber") pageNumber: Int
    ): ResponseWrapper<ListWrapper<PropertiesItem>>
}