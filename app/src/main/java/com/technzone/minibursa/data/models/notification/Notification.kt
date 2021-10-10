package com.technzone.minibursa.data.models.notification

import com.google.gson.annotations.SerializedName

data class Notification(

	@field:SerializedName("arabicTitle")
	val arabicTitle: String? = null,

	@field:SerializedName("createdDate")
	val createdDate: String? = null,

	@field:SerializedName("createdBy")
	val createdBy: String? = null,

	@field:SerializedName("investmentId")
	val investmentId: Int? = null,

	@field:SerializedName("englishBody")
	val englishBody: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("type")
	val type: Int? = null,

	@field:SerializedName("bookingId")
	val bookingId: Int? = null,

	@field:SerializedName("createdById")
	val createdById: String? = null,

	@field:SerializedName("arabicBody")
	val arabicBody: String? = null,

	@field:SerializedName("englishTitle")
	val englishTitle: String? = null
)