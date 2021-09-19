package com.technzone.miniborsa.data.repos.investors

import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.api.response.ResponseHandler
import com.technzone.miniborsa.data.api.response.ResponseWrapper
import com.technzone.miniborsa.data.daos.remote.investor.InvestorRemoteDao
import com.technzone.miniborsa.data.models.general.ListWrapper
import com.technzone.miniborsa.data.models.investor.Business
import com.technzone.miniborsa.data.models.investor.investors.Investor
import com.technzone.miniborsa.data.models.investor.investors.InvestorFilter
import com.technzone.miniborsa.data.repos.base.BaseRepo
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
        jobTitle: String?,
        investmentBudget: Double,
        InvestmentBudgetNA: Boolean,
        countries: List<Int>,
        categories: List<Int>
    ): APIResource<ResponseWrapper<Int>> {
        return try {
            responseHandle.handleSuccess(
                investorRemoteDao.becomeInvestor(
                    jobTitle,
                    investmentBudget,
                    InvestmentBudgetNA,
                    countries,
                    categories
                )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getBusinessByType(
        title: String?,
        businessType: Int?,
        AskingPriceRangeFrom: Int?,
        AskingPriceRangeTo: Int?,
        countries: List<Int>?,
        categories: List<Int>?,
        pageSize: Int,
        pageNumber: Int
    ): APIResource<ResponseWrapper<ListWrapper<Business>>> {
        return try {
            responseHandle.handleSuccess(
                investorRemoteDao.getBusinessByType(
                    title,
                    businessType,
                    AskingPriceRangeFrom,
                    AskingPriceRangeTo,
                    countries,
                    categories,
                    pageSize,
                    pageNumber
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

    override suspend fun getFavorites(
        pageSize: Int,
        pageNumber: Int
    ): APIResource<ResponseWrapper<ListWrapper<Business>>> {
        return try {
            responseHandle.handleSuccess(
                investorRemoteDao.getFavorites(
                    pageSize,
                    pageNumber
                )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }
}