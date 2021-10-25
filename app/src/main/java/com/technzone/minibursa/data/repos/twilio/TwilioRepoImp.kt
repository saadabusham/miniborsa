package com.technzone.minibursa.data.repos.twilio

import com.technzone.minibursa.data.api.response.APIResource
import com.technzone.minibursa.data.api.response.ResponseHandler
import com.technzone.minibursa.data.api.response.ResponseWrapper
import com.technzone.minibursa.data.common.Constants
import com.technzone.minibursa.data.daos.remote.twilio.TwilioRemoteDao
import com.technzone.minibursa.data.repos.base.BaseRepo
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

    override suspend fun getTwilioToken(
        pushCredentials: String,
        businessId: Int?
    ): APIResource<ResponseWrapper<String>> {
        return try {
            responseHandle.handleSuccess(
                twilioRemoteDao.getTwilioToken(
                    Constants.Twilio.CHAT_PUSH_CREDENTIALS,
                    businessId
                )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getBusinessChannelId(businessId: Int?): APIResource<ResponseWrapper<String>> {
        return try {
            responseHandle.handleSuccess(twilioRemoteDao.getBusinessChannelId(businessId))
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getInvestorChannelId(
        InvestorId: String?,
        businessId: Int?
    ): APIResource<ResponseWrapper<String>> {
        return try {
            responseHandle.handleSuccess(
                twilioRemoteDao.getInvestorChannelId(
                    InvestorId,
                    businessId
                )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }
}