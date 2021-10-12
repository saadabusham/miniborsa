package com.technzone.minibursa.data.models.plan

import com.google.gson.annotations.SerializedName

data class PromoCode(

	@field:SerializedName("consumed")
	val consumed: Boolean? = null,

	@field:SerializedName("subscriptions")
	val subscriptions: List<Any?>? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("endDate")
	val endDate: String? = null,

	@field:SerializedName("discount")
	val discount: Int? = null,

	@field:SerializedName("updatedDate")
	val updatedDate: String? = null,

	@field:SerializedName("updatedById")
	val updatedById: String? = null,

	@field:SerializedName("allowMultipleUsage")
	val allowMultipleUsage: Boolean? = null,

	@field:SerializedName("createdDate")
	val createdDate: String? = null,

	@field:SerializedName("isDeleted")
	val isDeleted: Boolean? = null,

	@field:SerializedName("deletionDate")
	val deletionDate: String? = null,

	@field:SerializedName("deletedById")
	val deletedById: String? = null,

	@field:SerializedName("uses")
	val uses: Int? = null,

	@field:SerializedName("discountType")
	val discountType: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("startDate")
	val startDate: String? = null,

	@field:SerializedName("discountLimit")
	val discountLimit: Int? = null,

	@field:SerializedName("createdById")
	val createdById: String? = null
)