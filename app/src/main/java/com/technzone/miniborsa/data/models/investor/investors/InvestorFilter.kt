package com.technzone.miniborsa.data.models.investor.investors

import com.google.gson.annotations.SerializedName
import com.technzone.miniborsa.data.common.Constants.PAGE_SIZE

data class InvestorFilter(

	@field:SerializedName("pageNumber")
	var pageNumber: Int = 1,

	@field:SerializedName("investmentBudget")
	val investmentBudget: Int? = null,

	@field:SerializedName("name")
	var name: String? = null,

	@field:SerializedName("pageSize")
	val pageSize: Int? = PAGE_SIZE,

	@field:SerializedName("countries")
	val countries: List<Int>? = null,

	@field:SerializedName("categories")
	val categories: List<Int>? = null,

	@field:SerializedName("investmentBudgetNA")
	val investmentBudgetNA: Boolean? = null,

	@field:SerializedName("parentId")
	val parentId: Int? = null
)