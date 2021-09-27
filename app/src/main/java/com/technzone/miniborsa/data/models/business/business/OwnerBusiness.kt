package com.technzone.miniborsa.data.models.business.business

import com.google.gson.annotations.SerializedName
import com.technzone.miniborsa.data.models.Media
import com.technzone.miniborsa.data.models.investor.FieldsItem
import com.technzone.miniborsa.data.models.investor.investors.CategoriesItem
import java.io.Serializable

data class OwnerBusiness(

	@field:SerializedName("businessId")
	val businessId: Int? = null,

	@field:SerializedName("websiteLink")
	val websiteLink: String? = null,

	@field:SerializedName("counrty")
	val counrty: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("englishDescription")
	val englishDescription: String? = null,

	@field:SerializedName("canRunfromHome")
	val canRunfromHome: Boolean? = null,

	@field:SerializedName("businessIcon")
	val icon: String? = null,

	@field:SerializedName("training")
	val training: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("isRelocated")
	val isRelocated: Boolean? = null,

	@field:SerializedName("askingPriceBoth")
	val askingPriceBoth: Int? = null,

	@field:SerializedName("investmentPercentage")
	val investmentPercentage: Int? = null,

	@field:SerializedName("propertyStatus")
	val propertyStatus: Int? = null,

	@field:SerializedName("status")
	val status: Int? = null,

	@field:SerializedName("rate")
	val rate: Int? = null,

	@field:SerializedName("askingPriceNABoth")
	val askingPriceNABoth: Boolean? = null,

	@field:SerializedName("annualTurnover")
	val annualTurnover: Int? = null,

	@field:SerializedName("listLocation")
	val listLocation: Boolean? = null,

	@field:SerializedName("annualNetProfitNA")
	val annualNetProfitNA: Boolean? = null,

	@field:SerializedName("establishedYear")
	val establishedYear: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("arabicDescription")
	val arabicDescription: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("isConfidential")
	val isConfidential: Boolean? = null,

	@field:SerializedName("askingPrice")
	val askingPrice: Int? = null,

	@field:SerializedName("askingPriceNA")
	val askingPriceNA: Boolean? = null,

	@field:SerializedName("annualNetProfit")
	val annualNetProfit: Int? = null,

	@field:SerializedName("annualTurnoverNA")
	val annualTurnoverNA: Boolean? = null,

	@field:SerializedName("fields", alternate = ["businessFields"])
	val fields: List<FieldsItem>? = null,

	@field:SerializedName("businessType")
	val businessType: Int? = null,

	@field:SerializedName("properties", alternate = ["businessProperties"])
	val properties: List<PropertiesItem>? = null,

	@field:SerializedName("images", alternate = ["businessImages"])
	val images: List<Media>? = null,

	@field:SerializedName("files", alternate = ["businessFiles"])
	val files: List<Media>? = null,

	@field:SerializedName("countries")
	val countries: List<Int>? = null,

	@field:SerializedName("categories")
	val categories: List<CategoriesItem>? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("isNegotiable")
	val isNegotiable: Boolean? = null

) : Serializable