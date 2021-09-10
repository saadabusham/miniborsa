package com.technzone.miniborsa.data.daos.remote.twilio

import com.technzone.miniborsa.data.api.response.ResponseWrapper
import io.reactivex.Single
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