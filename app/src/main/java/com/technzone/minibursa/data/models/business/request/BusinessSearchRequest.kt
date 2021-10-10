package com.technzone.minibursa.data.models.business.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BusinessSearchRequest(

	@field:SerializedName("durationFilter")
	val durationFilter: DurationFilter? = null,

	@field:SerializedName("companyId")
	val companyId: Int? = null,

	@field:SerializedName("pageNumber")
	val pageNumber: Int? = null,

	@field:SerializedName("businessId")
	val businessId: Int? = null,

	@field:SerializedName("statuses")
	val statuses: List<Int>? = null,

	@field:SerializedName("pageSize")
	val pageSize: Int? = null
):Serializable