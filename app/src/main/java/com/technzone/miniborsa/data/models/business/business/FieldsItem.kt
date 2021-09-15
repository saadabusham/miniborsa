package com.technzone.miniborsa.data.models.business.business

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FieldsItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("value")
	val value: String? = null
):Serializable