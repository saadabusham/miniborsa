package com.technzone.miniborsa.data.repos.twilio

import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.api.response.ResponseWrapper


interface TwilioRepo {

    suspend fun getAccessToken(
    ): APIResource<ResponseWrapper<String>>

    suspend fun getTwilioToken(
    ): APIResource<ResponseWrapper<String>>

}