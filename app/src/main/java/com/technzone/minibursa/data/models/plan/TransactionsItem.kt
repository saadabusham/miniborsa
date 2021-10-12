package com.technzone.minibursa.data.models.plan

import com.google.gson.annotations.SerializedName

data class TransactionsItem(

	@field:SerializedName("amount")
	val amount: Int? = null,

	@field:SerializedName("serviceCharge")
	val serviceCharge: Int? = null,

	@field:SerializedName("companyId")
	val companyId: Int? = null,

	@field:SerializedName("createdDate")
	val createdDate: String? = null,

	@field:SerializedName("extra")
	val extra: Int? = null,

	@field:SerializedName("tax")
	val tax: Int? = null,

	@field:SerializedName("subscription")
	val subscription: Subscription? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("type")
	val type: Int? = null,

	@field:SerializedName("subscriptionId")
	val subscriptionId: Int? = null,

	@field:SerializedName("createdById")
	val createdById: String? = null
)