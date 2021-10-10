package com.technzone.minibursa.data.models.investor

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Field(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
): Serializable