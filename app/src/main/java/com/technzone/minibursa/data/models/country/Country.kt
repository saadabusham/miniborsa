package com.technzone.minibursa.data.models.country

import com.google.gson.annotations.SerializedName

data class Country(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)