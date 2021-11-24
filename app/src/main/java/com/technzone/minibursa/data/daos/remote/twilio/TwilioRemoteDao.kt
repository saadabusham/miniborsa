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

    @POST("api/ChatChannels/investor")
    @FormUrlEncoded
    suspend fun getInvestorToBusinessChannelId(
        @Field("id") businessId: Int?
    ): ResponseWrapper<String>

    @POST("api/ChatChannels/businessOwner")
    @FormUrlEncoded
    suspend fun getBusinessToInvestorChannelId(
        @Field("id") InvestorId: String?,
        @Field("businessId") businessId: Int?
    ): ResponseWrapper<String>

}