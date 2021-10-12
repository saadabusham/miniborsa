package com.technzone.minibursa.data.models.plan

import com.google.gson.annotations.SerializedName

data class Country(

	@field:SerializedName("arabicName")
	val arabicName: String? = null,

	@field:SerializedName("englishName")
	val englishName: String? = null,

	@field:SerializedName("localizedName")
	val localizedName: String? = null,

	@field:SerializedName("createdDate")
	val createdDate: String? = null,

	@field:SerializedName("isDeleted")
	val isDeleted: Boolean? = null,

	@field:SerializedName("deletionDate")
	val deletionDate: String? = null,

	@field:SerializedName("deletedById")
	val deletedById: String? = null,

	@field:SerializedName("updatedDate")
	val updatedDate: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("updatedById")
	val updatedById: String? = null,

	@field:SerializedName("createdById")
	val createdById: String? = null
)