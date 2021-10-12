package com.technzone.minibursa.data.models.plan

import com.google.gson.annotations.SerializedName

data class Business(

	@field:SerializedName("websiteLink")
	val websiteLink: String? = null,

	@field:SerializedName("counrty")
	val counrty: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("englishDescription")
	val englishDescription: String? = null,

	@field:SerializedName("operationManagerId")
	val operationManagerId: String? = null,

	@field:SerializedName("canRunfromHome")
	val canRunfromHome: Boolean? = null,

	@field:SerializedName("icon")
	val icon: String? = null,

	@field:SerializedName("training")
	val training: String? = null,

	@field:SerializedName("updatedDate")
	val updatedDate: String? = null,

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

	@field:SerializedName("isDeleted")
	val isDeleted: Boolean? = null,

	@field:SerializedName("rate")
	val rate: Int? = null,

	@field:SerializedName("askingPriceNABoth")
	val askingPriceNABoth: Boolean? = null,

	@field:SerializedName("deletedById")
	val deletedById: String? = null,

	@field:SerializedName("annualTurnover")
	val annualTurnover: Int? = null,

	@field:SerializedName("listLocation")
	val listLocation: Boolean? = null,

	@field:SerializedName("annualNetProfitNA")
	val annualNetProfitNA: Boolean? = null,

	@field:SerializedName("establishedYear")
	val establishedYear: String? = null,

	@field:SerializedName("categories")
	val categories: List<CategoriesItem?>? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("arabicDescription")
	val arabicDescription: String? = null,

	@field:SerializedName("channelId")
	val channelId: String? = null,

	@field:SerializedName("createdById")
	val createdById: String? = null,

	@field:SerializedName("images")
	val images: List<ImagesItem?>? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("isConfidential")
	val isConfidential: Boolean? = null,

	@field:SerializedName("askingPrice")
	val askingPrice: Int? = null,

	@field:SerializedName("askingPriceNA")
	val askingPriceNA: Boolean? = null,

	@field:SerializedName("countries")
	val countries: List<CountriesItem?>? = null,

	@field:SerializedName("updatedById")
	val updatedById: String? = null,

	@field:SerializedName("annualNetProfit")
	val annualNetProfit: Int? = null,

	@field:SerializedName("annualTurnoverNA")
	val annualTurnoverNA: Boolean? = null,

	@field:SerializedName("companyId")
	val companyId: Int? = null,

	@field:SerializedName("createdDate")
	val createdDate: String? = null,

	@field:SerializedName("isNegotiable")
	val isNegotiable: Boolean? = null,

	@field:SerializedName("deletionDate")
	val deletionDate: String? = null,

	@field:SerializedName("files")
	val files: List<FilesItem?>? = null,

	@field:SerializedName("businessType")
	val businessType: Int? = null,

	@field:SerializedName("subscriptionId")
	val subscriptionId: Int? = null,

	@field:SerializedName("fields")
	val fields: List<FieldsItem?>? = null,

	@field:SerializedName("properties")
	val properties: List<PropertiesItem?>? = null,

	@field:SerializedName("managers")
	val managers: List<ManagersItem?>? = null
)