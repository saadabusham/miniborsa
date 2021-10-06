package com.technzone.miniborsa.data.models.investor

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.technzone.miniborsa.data.db.ApplicationDB
import com.technzone.miniborsa.data.models.Media
import com.technzone.miniborsa.data.models.business.business.PropertiesItem
import com.technzone.miniborsa.data.models.general.GeneralLookup
import com.technzone.miniborsa.data.models.investor.investors.CategoriesItem
import java.io.Serializable

@Entity(tableName = ApplicationDB.TABLE_SEARCHED_BUSINESS)
data class Business(

	@field:SerializedName("websiteLink")
	@ColumnInfo(name = "websiteLink")
    var websiteLink: String? = null,

	@field:SerializedName("counrty")
	@ColumnInfo(name = "counrty")
	val counrty: String? = null,

	@field:SerializedName("city")
	@ColumnInfo(name = "city")
	val city: String? = null,

	@field:SerializedName("englishDescription")
	@ColumnInfo(name = "englishDescription")
	val englishDescription: String? = null,

	@field:SerializedName("canRunfromHome")
	@ColumnInfo(name = "canRunfromHome")
	val canRunfromHome: Boolean? = null,

	@field:SerializedName("icon")
	@ColumnInfo(name = "icon")
	val icon: String? = null,

	@field:SerializedName("training")
	@ColumnInfo(name = "training")
	val training: String? = null,

	@field:SerializedName("title")
	@ColumnInfo(name = "title")
	val title: String? = null,

	@field:SerializedName("isRelocated")
	@ColumnInfo(name = "isRelocated")
	val isRelocated: Boolean? = null,

	@field:SerializedName("askingPriceBoth")
	@ColumnInfo(name = "askingPriceBoth")
	val askingPriceBoth: Double? = null,

	@field:SerializedName("investmentPercentage")
	@ColumnInfo(name = "investmentPercentage")
	val investmentPercentage: Int? = null,

	@field:SerializedName("propertyStatus")
	@ColumnInfo(name = "propertyStatus")
	val propertyStatus: Int? = null,

	@field:SerializedName("rate")
	@ColumnInfo(name = "rate")
	val rate: Int? = null,

	@field:SerializedName("askingPriceNABoth")
	@ColumnInfo(name = "askingPriceNABoth")
	val askingPriceNABoth: Boolean? = null,

	@field:SerializedName("annualTurnover")
	@ColumnInfo(name = "annualTurnover")
	val annualTurnover: Double? = null,

	@field:SerializedName("listLocation")
	@ColumnInfo(name = "listLocation")
	val listLocation: Boolean? = null,

	@field:SerializedName("company")
	@ColumnInfo(name = "company")
	val company: String? = null,

	@field:SerializedName("annualNetProfitNA")
	@ColumnInfo(name = "annualNetProfitNA")
	val annualNetProfitNA: Boolean? = null,

	@field:SerializedName("establishedYear")
	@ColumnInfo(name = "establishedYear")
	val establishedYear: String? = null,

	@field:SerializedName("id")
	@PrimaryKey(autoGenerate = false)
	@ColumnInfo(name = "id")
	val id: Int? = null,

	@field:SerializedName("arabicDescription")
	@ColumnInfo(name = "arabicDescription")
	val arabicDescription: String? = null,

	@field:SerializedName("address")
	@ColumnInfo(name = "address")
	val address: String? = null,

	@field:SerializedName("isConfidential")
	@ColumnInfo(name = "isConfidential")
	val isConfidential: Boolean? = null,

	@field:SerializedName("askingPrice")
	@ColumnInfo(name = "askingPrice")
	val askingPrice: Double? = null,

	@field:SerializedName("askingPriceNA")
	@ColumnInfo(name = "askingPriceNA")
	val askingPriceNA: Boolean? = null,

	@field:SerializedName("annualNetProfit")
	@ColumnInfo(name = "annualNetProfit")
	val annualNetProfit: Double? = null,

	@field:SerializedName("annualTurnoverNA")
	@ColumnInfo(name = "annualTurnoverNA")
	val annualTurnoverNA: Boolean? = null,

	@field:SerializedName("isFav")
	@ColumnInfo(name = "isFavorite")
	var isFavorite: Boolean? = null,

	@field:SerializedName("businessType")
	@ColumnInfo(name = "businessType")
	val businessType: Int? = null,

	@field:SerializedName("fields")
	@ColumnInfo(name = "fields")
	val fields: List<FieldsItem>? = null,

	@field:SerializedName("properties")
	@ColumnInfo(name = "properties")
	val properties: List<PropertiesItem>? = null,

	@field:SerializedName("images")
	@ColumnInfo(name = "images")
	var images: MutableList<Media>? = mutableListOf(),

	@field:SerializedName("files")
	@ColumnInfo(name = "files")
	val files: MutableList<Media>? = mutableListOf(),

	@field:SerializedName("countries")
	@ColumnInfo(name = "countries")
	val countries: List<GeneralLookup>? = null,

	@field:SerializedName("categories")
	@ColumnInfo(name = "categories")
	val categories: List<CategoriesItem>? = null

) : Serializable