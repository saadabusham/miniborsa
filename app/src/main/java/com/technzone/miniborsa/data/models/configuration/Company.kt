package com.technzone.miniborsa.data.models.configuration

import com.google.gson.annotations.SerializedName

data class Company(

	@field:SerializedName("twitter")
	val twitter: String? = null,

	@field:SerializedName("website")
	val website: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("englishDescription")
	val englishDescription: String? = null,

	@field:SerializedName("facebook")
	val facebook: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("logo")
	val logo: String? = null,

	@field:SerializedName("instagram")
	val instagram: String? = null,

	@field:SerializedName("arabicDescription")
	val arabicDescription: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)