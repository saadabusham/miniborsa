package com.technzone.miniborsa.data.repos.investors

import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.api.response.ResponseWrapper
import com.technzone.miniborsa.data.models.general.ListWrapper
import com.technzone.miniborsa.data.models.investor.Business
import com.technzone.miniborsa.data.models.investor.investors.Investor
import com.technzone.miniborsa.data.models.investor.investors.InvestorFilter


interface InvestorsRepo {

    suspend fun getInvestors(
        investorFilter: InvestorFilter
    ): APIResource<ResponseWrapper<ListWrapper<Investor>>>

    suspend fun getInvestor(
    ): APIResource<ResponseWrapper<Investor>>

    suspend fun getInvestorById(
        id: Int
    ): APIResource<ResponseWrapper<Investor>>

    suspend fun becomeInvestor(
        jobTitle: String?,
        bio: String?,
        investmentBudget: Double,
        InvestmentBudgetNA: Boolean,
        countries: List<Int>,
        categories: List<Int>,
        isOnline: Boolean
    ): APIResource<ResponseWrapper<Int>>

    suspend fun getBusinessByType(
        title: String? = null,
        businessType: Int? = null,
        askingPriceRangeFrom: Int? = null,
        askingPriceRangeTo: Int? = null,
        countries: List<Int>? = null,
        categories: List<Int>? = null,
        pageSize: Int,
        pageNumber: Int,
        gender: Int? = null,
        active: Boolean? = null,
        latitude: Int? = null,
        longitude: Int? = null
    ): APIResource<ResponseWrapper<ListWrapper<Business>>>

    suspend fun getBusinessById(
        id: Int
    ): APIResource<ResponseWrapper<Business>>

}