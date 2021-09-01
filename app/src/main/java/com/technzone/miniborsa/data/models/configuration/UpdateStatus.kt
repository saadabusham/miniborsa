package com.technzone.miniborsa.data.models.configuration

import com.squareup.moshi.Json

data class UpdateStatus(

	@field:Json(name ="iosAppUrl")
	val iosAppUrl: String? = null,

	@field:Json(name ="iosIsMandatory")
	val iosIsMandatory: Boolean? = null,

	@field:Json(name ="englishDescription")
	val englishDescription: String? = null,

	@field:Json(name ="appUrl")
	val appUrl: String? = null,

	@field:Json(name ="iosVersion")
	val iosVersion: String? = null,

	@field:Json(name ="arabicDescription")
	val arabicDescription: String? = null,

	@field:Json(name ="version")
	val version: String? = null,

	@field:Json(name ="isMandatory")
	val isMandatory: Boolean? = null
)