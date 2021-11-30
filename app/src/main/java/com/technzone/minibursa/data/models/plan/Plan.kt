package com.technzone.minibursa.data.models.plan

import com.google.gson.annotations.SerializedName

data class Plan(

	@field:SerializedName("planName")
	val planName: String? = null,

	@field:SerializedName("isAnnual")
	val isAnnual: Boolean? = null,

	@field:SerializedName("isPromoted")
	val isPromoted: Boolean? = null,

	@field:SerializedName("price")
	val price: Double? = null,

	@field:SerializedName("tax")
	val tax: Double? = null,

	@field:SerializedName("serviceCharge")
	val serviceCharge: Double? = null,

	@field:SerializedName("extra")
	val extra: Double? = null,

	@field:SerializedName("planFor")
	val planFor: Int? = null,

	@field:SerializedName("is_deleted")
	val isDeleted: Boolean? = null,

	@field:SerializedName("createdDate")
	val createdDate: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	var selected: Boolean = false
)