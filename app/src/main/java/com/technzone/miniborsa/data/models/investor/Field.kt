package com.technzone.miniborsa.data.models.investor

import com.squareup.moshi.Json
import java.io.Serializable

data class Field(

	@Json(name="name")
	val name: String? = null,

	@Json(name="id")
	val id: Int? = null
):Serializable