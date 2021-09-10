package com.technzone.miniborsa.data.repos.twilio

import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.api.response.ResponseHandler
import com.technzone.miniborsa.data.api.response.ResponseWrapper
import com.technzone.miniborsa.data.common.Constants
import com.technzone.miniborsa.data.daos.remote.twilio.TwilioRemoteDao
import com.technzone.miniborsa.data.repos.base.BaseRepo
import com.technzone.miniborsa.data.repos.twilio.TwilioRepo
import javax.inject.Inject


class TwilioRepoImp @Inject constructor(
    responseHandler: ResponseHandler,
    private val twilioRemoteDao: TwilioRemoteDao
) : BaseRepo(responseHandler), TwilioRepo {

    override suspend fun getAccessToken(): APIResource<ResponseWrapper<String>> {
        return try {
            responseHandle.handleSuccess(twilioRemoteDao.getAccessToken(Constants.Twilio.CHAT_PUSH_CREDENTIALS))
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getTwilioToken(): APIResource<ResponseWrapper<String>> {
        return try {
            responseHandle.handleSuccess(twilioRemoteDao.getTwilioToken(Constants.Twilio.CHAT_PUSH_CREDENTIALS))
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

}