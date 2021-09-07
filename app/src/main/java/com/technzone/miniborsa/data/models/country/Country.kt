package com.technzone.miniborsa.data.models.country

import com.google.gson.annotations.SerializedName

data class Country(

	@field:SerializedName("arabicName")
	val arabicName: String? = null,

	@field:SerializedName("englishName")
	val englishName: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)