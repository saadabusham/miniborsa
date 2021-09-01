package com.technzone.miniborsa.data.models.configuration

import com.squareup.moshi.Json

data class Developer(

	@field:Json(name ="twitter")
	val twitter: String? = null,

	@field:Json(name ="website")
	val website: String? = null,

	@field:Json(name ="phone")
	val phone: String? = null,

	@field:Json(name ="englishDescription")
	val englishDescription: String? = null,

	@field:Json(name ="facebook")
	val facebook: String? = null,

	@field:Json(name ="name")
	val name: String? = null,

	@field:Json(name ="logo")
	val logo: String? = null,

	@field:Json(name ="instagram")
	val instagram: String? = null,

	@field:Json(name ="arabicDescription")
	val arabicDescription: String? = null,

	@field:Json(name ="email")
	val email: String? = null
)