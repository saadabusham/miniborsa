package com.technzone.miniborsa.data.models.investor.investors

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CategoriesItem(

	@field:SerializedName("sortIndex")
	val sortIndex: Int? = null,

	@field:SerializedName("icon")
	val icon: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("active")
	val active: Boolean? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("parentId")
	val parentId: Int? = null
) : Serializable