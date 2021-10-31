package com.technzone.minibursa.data.repos.twilio

import com.technzone.minibursa.data.api.response.APIResource
import com.technzone.minibursa.data.api.response.ResponseWrapper
import retrofit2.http.Field
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface TwilioRepo {

    suspend fun getAccessToken(
    ): APIResource<ResponseWrapper<String>>

    suspend fun getTwilioToken(
        pushCredentials: String,
        businessId: Int?
    ): APIResource<ResponseWrapper<String>>

    suspend fun getInvestorToBusinessChannelId(
        businessId: Int?
    ): APIResource<ResponseWrapper<String>>

    suspend fun getBusinessToInvestorChannelId(
        InvestorId: String?,
        businessId: Int?
    ): APIResource<ResponseWrapper<String>>
}