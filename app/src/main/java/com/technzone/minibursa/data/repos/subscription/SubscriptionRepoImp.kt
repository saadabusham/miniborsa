package com.technzone.minibursa.data.repos.subscription

import com.technzone.minibursa.data.api.response.APIResource
import com.technzone.minibursa.data.api.response.ResponseHandler
import com.technzone.minibursa.data.api.response.ResponseWrapper
import com.technzone.minibursa.data.daos.remote.subscription.SubscriptionRemoteDao
import com.technzone.minibursa.data.models.plan.Plan
import com.technzone.minibursa.data.repos.base.BaseRepo
import javax.inject.Inject

class SubscriptionRepoImp @Inject constructor(
    responseHandler: ResponseHandler,
    private val subscriptionRemoteDao: SubscriptionRemoteDao
) : BaseRepo(responseHandler), SubscriptionRepo {

    override suspend fun getSubscription(
        planFor: Int
    ): APIResource<ResponseWrapper<List<Plan>>> {
        return try {
            responseHandle.handleSuccess(
                subscriptionRemoteDao.getSubscription(planFor)
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun createSubscription(
        businessId: Int,
        subscriptionPlanId: Int,
        promoCodeId: Int?
    ): APIResource<ResponseWrapper<Int>> {
        return try {
            responseHandle.handleSuccess(
                subscriptionRemoteDao.createSubscription(
                    businessId,
                    subscriptionPlanId,
                    promoCodeId
                )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun generateCheckoutId(
        amount: Double,
        currency: String,
        subscriptionId: Int
    ): APIResource<ResponseWrapper<String>> {
        return try {
            responseHandle.handleSuccess(
                subscriptionRemoteDao.generateCheckoutId(
                    amount,
                    currency,
                    subscriptionId
                )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getPaymentStatus(id: String): APIResource<ResponseWrapper<Any>> {
        return try {
            responseHandle.handleSuccess(
                subscriptionRemoteDao.getPaymentStatus(id)
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

}