package com.technzone.minibursa.data.models.plan

import com.google.gson.annotations.SerializedName

data class InvestmentRequestsItem(

	@field:SerializedName("business")
	val business: Business? = null,

	@field:SerializedName("businessId")
	val businessId: Int? = null,

	@field:SerializedName("updatedDate")
	val updatedDate: String? = null,

	@field:SerializedName("updatedById")
	val updatedById: String? = null,

	@field:SerializedName("propertyStatus")
	val propertyStatus: Int? = null,

	@field:SerializedName("companyId")
	val companyId: Int? = null,

	@field:SerializedName("createdDate")
	val createdDate: String? = null,

	@field:SerializedName("estimatedAskingPrice")
	val estimatedAskingPrice: Int? = null,

	@field:SerializedName("comment")
	val comment: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("businessType")
	val businessType: Int? = null,

	@field:SerializedName("createdById")
	val createdById: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)