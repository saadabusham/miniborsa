package com.technzone.minibursa.data.models.plan

import com.google.gson.annotations.SerializedName

data class ImagesItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("contentType")
	val contentType: String? = null,

	@field:SerializedName("parentId")
	val parentId: Int? = null
)