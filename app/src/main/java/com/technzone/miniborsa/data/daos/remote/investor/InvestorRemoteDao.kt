package com.technzone.miniborsa.data.daos.remote.investor

import com.technzone.miniborsa.data.api.response.ResponseWrapper
import com.technzone.miniborsa.data.common.NetworkConstants
import com.technzone.miniborsa.data.models.general.ListWrapper
import com.technzone.miniborsa.data.models.investor.Business
import com.technzone.miniborsa.data.models.investor.investors.Investor
import com.technzone.miniborsa.data.models.investor.investors.InvestorFilter
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
    @FormUrlEncoded
    @POST("api/Investors")
    suspend fun becomeInvestor(
        @Field("JobTitle") jobTitle: String?,
        @Field("Bio") bio: String?,
        @Field("InvestmentBudget") investmentBudget: Double,
        @Field("InvestmentBudgetNA") InvestmentBudgetNA: Boolean,
        @Field("Countries") countries: List<Int>,
        @Field("Categories") categories: List<Int>,
        @Field("IsOnline") isOnline: Boolean
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