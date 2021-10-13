package com.technzone.minibursa.data.daos.remote.twilio

import com.technzone.minibursa.data.api.response.ResponseWrapper
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TwilioRemoteDao {

    @GET("api/twilio/chat/token/{pushCredentials}")
    suspend fun getAccessToken(
        @Path("pushCredentials") pushCredentials: String
    ): ResponseWrapper<String>

    @GET("api/twilio/voice/token/{pushCredentials}")
    suspend fun getTwilioToken(
        @Path("pushCredentials") pushCredentials: String
    ): ResponseWrapper<String>

    @GET("api/chat")
    suspend fun getChannelId(
        @Query("businessId") businessId: Int?,
        @Query("InvestorId") InvestorId: Int?
    ): ResponseWrapper<String>

}