package com.technzone.minibursa.data.repos.subscription

import com.technzone.minibursa.data.api.response.APIResource
import com.technzone.minibursa.data.api.response.ResponseWrapper
import com.technzone.minibursa.data.models.plan.Plan
import retrofit2.http.Field

interface SubscriptionRepo {

    suspend fun getSubscription(
        planFor: Int
    ): APIResource<ResponseWrapper<List<Plan>>>

    suspend fun createSubscription(
        businessId :Int,
        subscriptionPlanId :Int,
        promoCodeId :Int?
    ): APIResource<ResponseWrapper<Int>>

    suspend fun generateCheckoutId(
        amount: Double,
        currency: String,
        subscriptionId: Int
    ): APIResource<ResponseWrapper<String>>

    suspend fun getPaymentStatus(
        id: String
    ): APIResource<ResponseWrapper<Any>>

}