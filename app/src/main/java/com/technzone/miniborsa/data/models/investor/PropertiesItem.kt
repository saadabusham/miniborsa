package com.technzone.miniborsa.data.models.investor

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PropertiesItem(

	@field:SerializedName("icon")
	val icon: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("parentId")
	val parentId: Int? = null
): Serializable