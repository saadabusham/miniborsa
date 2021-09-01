package com.technzone.miniborsa.data.models.configuration

import com.squareup.moshi.Json

data class ConfigSetting(

	@Json(name="serviceCharge")
	val serviceCharge: Double? = null,

	@Json(name="maximumPrice")
	val maximumPrice: Double? = null,

	@Json(name="tax")
	val tax: Double? = null,

	@Json(name="minimumPrice")
	val minimumPrice: Double? = null,

	@Json(name="showJetsList")
	val showJetsList: Boolean? = null
)