package com.technzone.minibursa.data.models.configuration

import com.google.gson.annotations.SerializedName

data class ConfigSetting(

	@field:SerializedName("serviceCharge")
	val serviceCharge: Int? = null,

	@field:SerializedName("showBusinesssList")
	val showBusinesssList: Boolean? = null,

	@field:SerializedName("maximumPrice")
	val maximumPrice: Int? = null,

	@field:SerializedName("tax")
	val tax: Int? = null,

	@field:SerializedName("minimumPrice")
	val minimumPrice: Int? = null
)