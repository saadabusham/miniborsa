package com.technzone.minibursa.data.models.plan

import com.google.gson.annotations.SerializedName

data class ManagersItem(

	@field:SerializedName("operationManagerId")
	val operationManagerId: String? = null,

	@field:SerializedName("businessId")
	val businessId: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null
)