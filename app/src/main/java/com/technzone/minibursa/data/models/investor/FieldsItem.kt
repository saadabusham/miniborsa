package com.technzone.minibursa.data.models.investor

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FieldsItem(

	@field:SerializedName("field")
	val field: Field? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("value")
	val value: String? = null
): Serializable