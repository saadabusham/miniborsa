package com.technzone.minibursa.data.daos.remote.subscription

import com.technzone.minibursa.data.api.response.ResponseWrapper
import com.technzone.minibursa.data.common.NetworkConstants
import com.technzone.minibursa.data.models.plan.Plan
import retrofit2.http.*

interface SubscriptionRemoteDao {

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("api/Subscriptions/plan/{planFor}")
    suspend fun getSubscription(
        @Path("planFor") planFor: Int
    ): ResponseWrapper<List<Plan>>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @FormUrlEncoded
    @POST("api/Subscriptions")
    suspend fun createSubscription(
        @Field("BusinessId") businessId: Int,
        @Field("SubscriptionPlanId") subscriptionPlanId: Int,
        @Field("PromoCodeId") promoCodeId: Int?
    ): ResponseWrapper<Int>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @FormUrlEncoded
    @POST("api/Payment/generateCheckoutId")
    suspend fun generateCheckoutId(
        @Field("Amount") amount: Double,
        @Field("Currency") currency: String,
        @Field("SubscriptionId") subscriptionId: Int
    ): ResponseWrapper<String>


    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("api/payment/getPaymentStatus/{id}")
    suspend fun getPaymentStatus(
        @Path("id") id: String
    ): ResponseWrapper<Any>

}