package com.technzone.minibursa.data.daos.remote.investor

import com.technzone.minibursa.data.api.response.ResponseWrapper
import com.technzone.minibursa.data.common.NetworkConstants
import com.technzone.minibursa.data.models.general.ListWrapper
import com.technzone.minibursa.data.models.investor.Business
import com.technzone.minibursa.data.models.investor.investors.Investor
import com.technzone.minibursa.data.models.investor.investors.InvestorFilter
import com.technzone.minibursa.data.models.investor.request.BecomeInvestorRequest
import retrofit2.http.*

interface InvestorRemoteDao {

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @POST("api/Investors/List")
    suspend fun getInvestors(
        @Body investorFilter: InvestorFilter
    ): ResponseWrapper<ListWrapper<Investor>>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("api/Investors")
    suspend fun getInvestor(
    ): ResponseWrapper<Investor>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("api/Investors/{id}")
    suspend fun getInvestorById(
        @Path("id") id: Int
    ): ResponseWrapper<Investor>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @POST("api/Investors")
    suspend fun becomeInvestor(
     @Body becomeInvestorRequest: BecomeInvestorRequest
    ): ResponseWrapper<Int>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("api/Business")
    suspend fun getBusinessByType(
        @Query("Title") title: String?,
        @Query("BusinessType") businessType: Int?,
        @Query("AskingPriceRange.From") askingPriceRangeFrom: Int?,
        @Query("AskingPriceRange.To") askingPriceRangeTo: Int?,
        @Query("Countries") countries: List<Int>?,
        @Query("Categories") categories: List<Int>?,
        @Query("PageSize") pageSize: Int,
        @Query("PageNumber") pageNumber: Int,
        @Query("Gender") gender: Int?,
        @Query("Active") active: Boolean?,
        @Query("Latitude") latitude: Int?,
        @Query("Longitude") longitude: Int?,
    ): ResponseWrapper<ListWrapper<Business>>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("api/Business/{id}")
    suspend fun getBusinessById(
        @Path("id") id: Int
    ): ResponseWrapper<Business>

}