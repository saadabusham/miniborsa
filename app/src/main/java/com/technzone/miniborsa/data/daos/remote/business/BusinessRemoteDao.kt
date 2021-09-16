package com.technzone.miniborsa.data.daos.remote.business

import com.technzone.miniborsa.data.api.response.ResponseWrapper
import com.technzone.miniborsa.data.common.NetworkConstants
import com.technzone.miniborsa.data.models.investor.investors.Investor
import com.technzone.miniborsa.data.models.investor.investors.InvestorFilter
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface BusinessRemoteDao {

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @POST("api/Investors")
    suspend fun getInvestors(
        @Body investorFilter: InvestorFilter
    ): ResponseWrapper<List<Investor>>

}