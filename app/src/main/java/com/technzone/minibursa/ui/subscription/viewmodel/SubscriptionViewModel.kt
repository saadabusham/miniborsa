package com.technzone.minibursa.ui.subscription.viewmodel

import androidx.lifecycle.liveData
import com.technzone.minibursa.data.api.response.APIResource
import com.technzone.minibursa.data.repos.subscription.SubscriptionRepo
import com.technzone.minibursa.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    private val subscriptionRepo: SubscriptionRepo
) : BaseViewModel() {

    fun getSubscription(planFor: Int) = liveData {
        emit(APIResource.loading())
        val response =
            subscriptionRepo.getSubscription(planFor)
        emit(response)
    }

    fun createSubscription(
        businessId: Int,
        subscriptionPlanId: Int,
        promoCodeId: Int? = null
    ) = liveData {
        emit(APIResource.loading())
        val response =
            subscriptionRepo.createSubscription(businessId, subscriptionPlanId, promoCodeId)
        emit(response)
    }

    fun generateCheckoutId(
        amount: Double,
        currency: String,
        subscriptionId: Int
    ) = liveData {
        emit(APIResource.loading())
        val response = subscriptionRepo.generateCheckoutId(amount, currency, subscriptionId)
        emit(response)
    }

    fun getPaymentStatus(
        checkoutId: String
    ) = liveData {
        emit(APIResource.loading())
        val response = subscriptionRepo.getPaymentStatus(
            checkoutId
        )
        emit(response)
    }
}