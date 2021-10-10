package com.technzone.minibursa.data.repos.twilio

import com.technzone.minibursa.data.api.response.APIResource
import com.technzone.minibursa.data.api.response.ResponseWrapper


interface TwilioRepo {

    suspend fun getAccessToken(
    ): APIResource<ResponseWrapper<String>>

    suspend fun getTwilioToken(
    ): APIResource<ResponseWrapper<String>>

}