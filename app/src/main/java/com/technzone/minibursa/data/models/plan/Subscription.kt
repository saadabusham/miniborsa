package com.technzone.minibursa.data.models.plan

import com.google.gson.annotations.SerializedName

data class Subscription(

	@field:SerializedName("notes")
	val notes: String? = null,

	@field:SerializedName("business")
	val business: Business? = null,

	@field:SerializedName("payments")
	val payments: List<PaymentsItem?>? = null,

	@field:SerializedName("businessId")
	val businessId: Int? = null,

	@field:SerializedName("tax")
	val tax: Int? = null,

	@field:SerializedName("updatedDate")
	val updatedDate: String? = null,

	@field:SerializedName("updatedById")
	val updatedById: String? = null,

	@field:SerializedName("expiryDate")
	val expiryDate: String? = null,

	@field:SerializedName("isPromoted")
	val isPromoted: Boolean? = null,

	@field:SerializedName("serviceCharge")
	val serviceCharge: Int? = null,

	@field:SerializedName("createdDate")
	val createdDate: String? = null,

	@field:SerializedName("promoCodeId")
	val promoCodeId: Int? = null,

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("extra")
	val extra: Int? = null,

	@field:SerializedName("promoCode")
	val promoCode: PromoCode? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("isAnnual")
	val isAnnual: Boolean? = null,

	@field:SerializedName("createdById")
	val createdById: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)