package com.technzone.minibursa.data.models.plan

import com.google.gson.annotations.SerializedName

data class PropertiesItem(

	@field:SerializedName("property")
	val property: Property? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("propertyId")
	val propertyId: Int? = null,

	@field:SerializedName("businessRequestId")
	val businessRequestId: Int? = null
)