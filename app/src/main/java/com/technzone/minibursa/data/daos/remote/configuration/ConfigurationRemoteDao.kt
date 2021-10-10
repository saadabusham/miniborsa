package com.technzone.minibursa.data.daos.remote.configuration

import com.technzone.minibursa.data.api.response.ResponseWrapper
import com.technzone.minibursa.data.models.business.business.PropertiesItem
import com.technzone.minibursa.data.models.configuration.ConfigurationWrapperResponse
import com.technzone.minibursa.data.models.country.Country
import com.technzone.minibursa.data.models.general.ListWrapper
import com.technzone.minibursa.data.models.investor.investors.CategoriesItem
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

    @GET("api/Investors/category")
    suspend fun getInvestorCategories(
        @Query("parentId") parentId:Int?,
        @Query("Name") name:String?,
        @Query("PageSize") pageSize: Int,
        @Query("PageNumber") pageNumber: Int
    ): ResponseWrapper<ListWrapper<CategoriesItem>>

    @GET("api/Property")
    suspend fun getProperty(
        @Query("parentId") parentId:Int?,
        @Query("Name") name:String?,
        @Query("PageSize") pageSize: Int,
        @Query("PageNumber") pageNumber: Int
    ): ResponseWrapper<ListWrapper<PropertiesItem>>
}