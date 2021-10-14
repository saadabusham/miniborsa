package com.technzone.minibursa.data.daos.remote.twilio

import com.technzone.minibursa.data.api.response.ResponseWrapper
import retrofit2.http.*

interface TwilioRemoteDao {

    @GET("api/twilio/chat/token/{pushCredentials}")
    suspend fun getAccessToken(
        @Path("pushCredentials") pushCredentials: String
    ): ResponseWrapper<String>

    @POST("api/twilio/chat/token")
    @FormUrlEncoded
    suspend fun getTwilioToken(
        @Field("pushCredentials") pushCredentials: String,
        @Field("businessId") businessId: Int?
    ): ResponseWrapper<String>

    @POST("api/ChatChannels/business/{id}")
    suspend fun getBusinessChannelId(
        @Path("id") businessId: Int?
    ): ResponseWrapper<String>

    @POST("api/ChatChannels/{id}")
    suspend fun getInvestorChannelId(
        @Path("id") InvestorId: String?
    ): ResponseWrapper<String>

}