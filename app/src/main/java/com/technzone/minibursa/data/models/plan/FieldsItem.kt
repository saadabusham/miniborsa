package com.technzone.minibursa.data.models.plan

import com.google.gson.annotations.SerializedName

data class FieldsItem(

	@field:SerializedName("field")
	val field: Field? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("value")
	val value: String? = null,

	@field:SerializedName("fieldId")
	val fieldId: Int? = null,

	@field:SerializedName("businessRequestId")
	val businessRequestId: Int? = null
)