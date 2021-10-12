package com.technzone.minibursa.data.daos.remote.subscription

import com.technzone.minibursa.data.api.response.ResponseWrapper
import com.technzone.minibursa.data.common.NetworkConstants
import com.technzone.minibursa.data.models.business.business.OwnerBusiness
import com.technzone.minibursa.data.models.business.businessrequest.BusinessRequest
import com.technzone.minibursa.data.models.business.request.BusinessSearchRequest
import com.technzone.minibursa.data.models.general.ListWrapper
import com.technzone.minibursa.data.models.plan.Plan
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface SubscriptionRemoteDao {

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("api/Subscriptions/plan")
    suspend fun getSubscription(
    ): ResponseWrapper<List<Plan>>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @FormUrlEncoded
    @POST("api/Subscriptions")
    suspend fun createSubscription(
        @Field("BusinessId") businessId :Int,
        @Field("SubscriptionPlanId") subscriptionPlanId :Int,
        @Field("PromoCodeId") promoCodeId :Int?
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
    @FormUrlEncoded
    @POST("api/payment/getPaymentStatus/{id}")
    suspend fun getPaymentStatus(
        @Path("id") id: String
    ): ResponseWrapper<Any>

}