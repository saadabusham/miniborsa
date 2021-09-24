package com.technzone.miniborsa.data.models.investor.investors

import com.google.gson.annotations.SerializedName

data class Investor(

	@field:SerializedName("lastName")
	val lastName: String? = null,

	@field:SerializedName("jobTitle")
	val jobTitle: String? = null,

	@field:SerializedName("fullName")
	val fullName: String? = null,

	@field:SerializedName("bio")
	val bio: String? = null,

	@field:SerializedName("isOnline")
	val isOnline: Boolean? = null,

	@field:SerializedName("countries")
	val countries: List<CountriesItem>? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("picture")
	val picture: String? = null,

	@field:SerializedName("firstName")
	val firstName: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("phoneNumber")
	val phoneNumber: String? = null,

	@field:SerializedName("investmentBudget")
	val investmentBudget: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("categories")
	val categories: List<CategoriesItem>? = null,

	@field:SerializedName("isFeatured")
	val isFeatured: Boolean? = null,

	@field:SerializedName("investmentBudgetNA")
	val investmentBudgetNA: Boolean? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)