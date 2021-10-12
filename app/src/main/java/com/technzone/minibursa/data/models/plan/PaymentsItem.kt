package com.technzone.minibursa.data.models.plan

import com.google.gson.annotations.SerializedName

data class PaymentsItem(

	@field:SerializedName("amount")
	val amount: Int? = null,

	@field:SerializedName("createdDate")
	val createdDate: String? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("updatedDate")
	val updatedDate: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("subscriptionId")
	val subscriptionId: Int? = null,

	@field:SerializedName("checkoutId")
	val checkoutId: String? = null,

	@field:SerializedName("updatedById")
	val updatedById: String? = null,

	@field:SerializedName("createdById")
	val createdById: String? = null,

	@field:SerializedName("paymentType")
	val paymentType: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)