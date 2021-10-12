package com.technzone.minibursa.data.models.business

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Subscription(

	@field:SerializedName("notes")
	val notes: String? = null,

	@field:SerializedName("updatedBy")
	val updatedBy: String? = null,

	@field:SerializedName("totalPrice")
	val totalPrice: Int? = null,

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

	@field:SerializedName("createdBy")
	val createdBy: String? = null,

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("extra")
	val extra: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("isAnnual")
	val isAnnual: Boolean? = null,

	@field:SerializedName("createdById")
	val createdById: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
):Serializable