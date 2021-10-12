package com.technzone.minibursa.data.models.plan

import com.google.gson.annotations.SerializedName

data class CountriesItem(

	@field:SerializedName("country")
	val country: Country? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("countryId")
	val countryId: Int? = null,

	@field:SerializedName("businessRequestId")
	val businessRequestId: Int? = null
)