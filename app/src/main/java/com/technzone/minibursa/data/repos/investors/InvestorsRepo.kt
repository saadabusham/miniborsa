package com.technzone.minibursa.data.repos.investors

import com.technzone.minibursa.data.api.response.APIResource
import com.technzone.minibursa.data.api.response.ResponseWrapper
import com.technzone.minibursa.data.models.general.ListWrapper
import com.technzone.minibursa.data.models.investor.Business
import com.technzone.minibursa.data.models.investor.investors.Investor
import com.technzone.minibursa.data.models.investor.investors.InvestorFilter


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