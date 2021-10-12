package com.technzone.minibursa.data.models.plan

import com.google.gson.annotations.SerializedName

data class UserRolesItem(

	@field:SerializedName("role")
	val role: Role? = null,

	@field:SerializedName("roleId")
	val roleId: Int? = null,

	@field:SerializedName("userId")
	val userId: String? = null
)