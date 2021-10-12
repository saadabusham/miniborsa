package com.technzone.minibursa.data.models.plan

import com.google.gson.annotations.SerializedName

data class CategoriesItem(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("category")
	val category: Category? = null,

	@field:SerializedName("categoryId")
	val categoryId: Int? = null,

	@field:SerializedName("businessRequestId")
	val businessRequestId: Int? = null
)