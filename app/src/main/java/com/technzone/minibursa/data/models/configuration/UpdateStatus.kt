package com.technzone.minibursa.data.models.configuration

import com.google.gson.annotations.SerializedName

data class UpdateStatus(

	@field:SerializedName("iosAppUrl")
	val iosAppUrl: String? = null,

	@field:SerializedName("iosIsMandatory")
	val iosIsMandatory: Boolean? = null,

	@field:SerializedName("englishDescription")
	val englishDescription: String? = null,

	@field:SerializedName("appUrl")
	val appUrl: String? = null,

	@field:SerializedName("iosVersion")
	val iosVersion: String? = null,

	@field:SerializedName("arabicDescription")
	val arabicDescription: String? = null,

	@field:SerializedName("version")
	val version: String? = null,

	@field:SerializedName("isMandatory")
	val isMandatory: Boolean? = null
)