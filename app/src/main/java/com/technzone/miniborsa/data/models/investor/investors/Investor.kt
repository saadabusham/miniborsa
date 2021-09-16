package com.technzone.miniborsa.data.models.investor.investors

import com.google.gson.annotations.SerializedName

data class Investor(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("jobTitle")
	val jobTitle: String? = null,

	@field:SerializedName("investmentBudget")
	val investmentBudget: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("countries")
	val countries: List<CountriesItem>? = null,

	@field:SerializedName("categories")
	val categories: List<CategoriesItem>? = null,

	@field:SerializedName("investmentBudgetNA")
	val investmentBudgetNA: Boolean? = null
)