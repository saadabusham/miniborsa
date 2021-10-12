package com.technzone.minibursa.data.models.plan

import com.google.gson.annotations.SerializedName

data class CardsItem(

	@field:SerializedName("bin")
	val bin: String? = null,

	@field:SerializedName("expiryMonth")
	val expiryMonth: String? = null,

	@field:SerializedName("holder")
	val holder: String? = null,

	@field:SerializedName("expiryYear")
	val expiryYear: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("last4Digits")
	val last4Digits: String? = null
)