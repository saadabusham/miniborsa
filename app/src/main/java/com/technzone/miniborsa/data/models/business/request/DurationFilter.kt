package com.technzone.miniborsa.data.models.business.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DurationFilter(

	@field:SerializedName("from")
	val from: String? = null,

	@field:SerializedName("to")
	val to: String? = null
):Serializable