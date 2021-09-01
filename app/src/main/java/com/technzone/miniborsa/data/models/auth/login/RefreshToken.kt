package com.technzone.miniborsa.data.models.auth.login

import com.squareup.moshi.Json

data class RefreshToken(

	@field:Json(name ="validUntil")
	val validUntil: String? = null,

	@field:Json(name ="refreshToken")
	val refreshToken: String? = null
)