package com.technzone.minibursa.data.models.plan

import com.google.gson.annotations.SerializedName

data class Plan(

	@field:SerializedName("updatedBy")
	val updatedBy: UpdatedBy? = null,

	@field:SerializedName("planName")
	val planName: String? = null,

	@field:SerializedName("tax")
	val tax: Int? = null,

	@field:SerializedName("updatedDate")
	val updatedDate: String? = null,

	@field:SerializedName("updatedById")
	val updatedById: String? = null,

	@field:SerializedName("planFor")
	val planFor: Int? = null,

	@field:SerializedName("isPromoted")
	val isPromoted: Boolean? = null,

	@field:SerializedName("serviceCharge")
	val serviceCharge: Int? = null,

	@field:SerializedName("is_deleted")
	val isDeleted: Boolean? = null,

	@field:SerializedName("createdDate")
	val createdDate: String? = null,

	@field:SerializedName("createdBy")
	val createdBy: CreatedBy? = null,

	@field:SerializedName("price")
	val price: Double? = null,

	@field:SerializedName("extra")
	val extra: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("isAnnual")
	val isAnnual: Boolean? = null,

	@field:SerializedName("createdById")
	val createdById: String? = null,

	var selected: Boolean = false
)