package com.technzone.miniborsa.data.models.investor.request

import com.google.gson.annotations.SerializedName

data class FavoriteRequest(

	@field:SerializedName("businessId")
	val businessId: Int? = null,

	@field:SerializedName("userId")
	val userId: String? = null
)