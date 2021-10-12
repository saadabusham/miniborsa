package com.technzone.minibursa.data.models.plan

import com.google.gson.annotations.SerializedName

data class Company(

	@field:SerializedName("businesss")
	val businesss: List<BusinesssItem?>? = null,

	@field:SerializedName("flyUsPercentage")
	val flyUsPercentage: Int? = null,

	@field:SerializedName("updatedDate")
	val updatedDate: String? = null,

	@field:SerializedName("isActive")
	val isActive: Boolean? = null,

	@field:SerializedName("transactions")
	val transactions: List<TransactionsItem?>? = null,

	@field:SerializedName("updatedById")
	val updatedById: String? = null,

	@field:SerializedName("users")
	val users: List<Any?>? = null,

	@field:SerializedName("investmentRequests")
	val investmentRequests: List<InvestmentRequestsItem?>? = null,

	@field:SerializedName("createdDate")
	val createdDate: String? = null,

	@field:SerializedName("isDeleted")
	val isDeleted: Boolean? = null,

	@field:SerializedName("deletionDate")
	val deletionDate: String? = null,

	@field:SerializedName("businessRequests")
	val businessRequests: List<BusinessRequestsItem?>? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("deletedById")
	val deletedById: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("createdById")
	val createdById: String? = null
)