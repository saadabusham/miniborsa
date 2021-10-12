package com.technzone.minibursa.data.models.plan

import com.google.gson.annotations.SerializedName

data class Role(

	@field:SerializedName("userRoles")
	val userRoles: List<Any?>? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)