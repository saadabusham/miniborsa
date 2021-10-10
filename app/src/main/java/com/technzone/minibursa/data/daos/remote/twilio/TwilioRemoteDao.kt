package com.technzone.minibursa.data.daos.remote.twilio

import com.technzone.minibursa.data.api.response.ResponseWrapper
import retrofit2.http.GET
import retrofit2.http.Path

interface TwilioRemoteDao {

    @GET("api/twilio/chat/token/{pushCredentials}")
    suspend fun getAccessToken(
        @Path("pushCredentials") pushCredentials: String
    ): ResponseWrapper<String>

    @GET("api/twilio/voice/token/{pushCredentials}")
    suspend fun getTwilioToken(
        @Path("pushCredentials") pushCredentials: String
    ): ResponseWrapper<String>

}