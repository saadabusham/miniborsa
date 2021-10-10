package com.technzone.minibursa.data.repos.investors

import com.technzone.minibursa.data.api.response.APIResource
import com.technzone.minibursa.data.api.response.ResponseHandler
import com.technzone.minibursa.data.api.response.ResponseWrapper
import com.technzone.minibursa.data.daos.remote.investor.InvestorRemoteDao
import com.technzone.minibursa.data.models.general.ListWrapper
import com.technzone.minibursa.data.models.investor.Business
import com.technzone.minibursa.data.models.investor.investors.Investor
import com.technzone.minibursa.data.models.investor.investors.InvestorFilter
import com.technzone.minibursa.data.repos.base.BaseRepo
import javax.inject.Inject

class InvestorsRepoImp @Inject constructor(
    responseHandler: ResponseHandler,
    private val investorRemoteDao: InvestorRemoteDao
) : BaseRepo(responseHandler), InvestorsRepo {

    override suspend fun getInvestors(investorFilter: InvestorFilter)
            : APIResource<ResponseWrapper<ListWrapper<Investor>>> {
        return try {
            responseHandle.handleSuccess(
                investorRemoteDao.getInvestors(investorFilter)
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getInvestor(): APIResource<ResponseWrapper<Investor>> {
        return try {
            responseHandle.handleSuccess(
                investorRemoteDao.getInvestor()
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getInvestorById(id: Int): APIResource<ResponseWrapper<Investor>> {
        return try {
            responseHandle.handleSuccess(
                investorRemoteDao.getInvestorById(id)
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun becomeInvestor(
        jobTitle:String?,
        bio: String?,
        investmentBudget: Double,
        InvestmentBudgetNA: Boolean,
        countries: List<Int>,
        categories: List<Int>,
        isOnline: Boolean
    ): APIResource<ResponseWrapper<Int>> {
        return try {
            responseHandle.handleSuccess(
                investorRemoteDao.becomeInvestor(
                    jobTitle,
                    bio,
                    investmentBudget,
                    InvestmentBudgetNA,
                    countries,
                    categories,
                    isOnline
                )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getBusinessByType(
        title: String?,
        businessType: Int?,
        askingPriceRangeFrom: Int?,
        askingPriceRangeTo: Int?,
        countries: List<Int>?,
        categories: List<Int>?,
        pageSize: Int,
        pageNumber: Int,
        gender: Int?,
        active: Boolean?,
        latitude: Int?,
        longitude: Int?
    ): APIResource<ResponseWrapper<ListWrapper<Business>>> {
        return try {
            responseHandle.handleSuccess(
                investorRemoteDao.getBusinessByType(
                    title,
                    businessType,
                    askingPriceRangeFrom,
                    askingPriceRangeTo,
                    countries,
                    categories,
                    pageSize,
                    pageNumber,
                    gender,
                    active,
                    latitude,
                    longitude
                )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getBusinessById(id: Int): APIResource<ResponseWrapper<Business>> {
        return try {
            responseHandle.handleSuccess(
                investorRemoteDao.getBusinessById(id)
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

}