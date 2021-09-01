package com.technzone.miniborsa.data.models.auth.login

import com.squareup.moshi.Json

data class RolesItem(

	@field:Json(name ="name")
	val name: String? = null,

	@field:Json(name ="id")
	val id: Int? = null
)