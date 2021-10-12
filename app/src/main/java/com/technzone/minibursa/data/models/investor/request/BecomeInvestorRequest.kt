package com.technzone.minibursa.data.models.investor.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BecomeInvestorRequest(

	@field:SerializedName("jobTitle")
	val jobTitle: String? = null,

	@field:SerializedName("investmentBudget")
	val investmentBudget: Double? = null,

	@field:SerializedName("bio")
	val bio: String? = null,

	@field:SerializedName("isOnline")
	val isOnline: Boolean? = null,

	@field:SerializedName("countries")
	val countries: List<Int>? = null,

	@field:SerializedName("categories")
	val categories: List<Int>? = null,

	@field:SerializedName("investmentBudgetNA")
	val investmentBudgetNA: Boolean? = null
):Serializable