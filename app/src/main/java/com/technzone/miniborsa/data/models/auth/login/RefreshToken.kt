package com.technzone.miniborsa.data.models.auth.login

import com.google.gson.annotations.SerializedName

data class RefreshToken(

	@field:SerializedName("validUntil")
	val validUntil: String? = null,

	@field:SerializedName("refreshToken")
	val refreshToken: String? = null
)