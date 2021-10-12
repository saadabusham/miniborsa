package com.technzone.minibursa.data.models.plan

import com.google.gson.annotations.SerializedName

data class NotificationsItem(

	@field:SerializedName("arabicTitle")
	val arabicTitle: String? = null,

	@field:SerializedName("createdDate")
	val createdDate: String? = null,

	@field:SerializedName("englishBody")
	val englishBody: String? = null,

	@field:SerializedName("businessId")
	val businessId: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("type")
	val type: Int? = null,

	@field:SerializedName("subscriptionId")
	val subscriptionId: Int? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("createdById")
	val createdById: String? = null,

	@field:SerializedName("arabicBody")
	val arabicBody: String? = null,

	@field:SerializedName("englishTitle")
	val englishTitle: String? = null
)