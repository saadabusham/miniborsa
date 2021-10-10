package com.technzone.minibursa.data.models.configuration

import com.google.gson.annotations.SerializedName

data class ConfigurationWrapperResponse(

	@field:SerializedName("configSetting")
	val configSetting: ConfigSetting? = null,

	@field:SerializedName("configString")
	val configString: ConfigString? = null,

	@field:SerializedName("updateStatus")
	val updateStatus: UpdateStatus? = null,

	@field:SerializedName("developer")
	val developer: Developer? = null,

	@field:SerializedName("company")
	val company: Company? = null,

	@field:SerializedName("id")
	val id: Int? = null
)