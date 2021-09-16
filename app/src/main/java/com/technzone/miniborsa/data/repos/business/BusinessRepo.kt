package com.technzone.miniborsa.data.repos.business

import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.api.response.ResponseWrapper
import com.technzone.miniborsa.data.enums.UserEnums
import com.technzone.miniborsa.data.models.auth.login.UserDetailsResponseModel
import com.technzone.miniborsa.data.models.investor.investors.Investor
import com.technzone.miniborsa.data.models.investor.investors.InvestorFilter
import okhttp3.MultipartBody
import okhttp3.RequestBody


interface BusinessRepo {

    suspend fun getInvestors(
        investorFilter: InvestorFilter
    ): APIResource<ResponseWrapper<List<Investor>>>

}