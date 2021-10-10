package com.technzone.minibursa.data.models.investor.investors

import com.google.gson.annotations.SerializedName
import com.technzone.minibursa.data.common.Constants.PAGE_SIZE
import com.technzone.minibursa.data.models.general.GeneralLookup

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
	var countries: List<Int>? = null,

	@field:SerializedName("selectedCountries")
	var selectedCountries: List<GeneralLookup>? = null,

	@field:SerializedName("categories")
	val categories: List<Int>? = null,

	@field:SerializedName("investmentBudgetNA")
	val investmentBudgetNA: Boolean? = null,

	@field:SerializedName("isFeatured")
	var isFeatured: Boolean? = null,

	@field:SerializedName("parentId")
	val parentId: Int? = null,

	@field:SerializedName("sortType")
	var sortType: Int? = null
)