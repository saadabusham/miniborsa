package com.technzone.miniborsa.data.models.investor

import com.google.gson.annotations.SerializedName

data class GeneralRequest(

	@field:SerializedName("pageNumber")
	val pageNumber: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("pageSize")
	val pageSize: Int? = null,

	@field:SerializedName("parentId")
	val parentId: Int? = null
)