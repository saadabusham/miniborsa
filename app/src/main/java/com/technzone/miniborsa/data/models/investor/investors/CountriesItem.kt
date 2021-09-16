package com.technzone.miniborsa.data.models.investor.investors

import com.google.gson.annotations.SerializedName

data class CountriesItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)