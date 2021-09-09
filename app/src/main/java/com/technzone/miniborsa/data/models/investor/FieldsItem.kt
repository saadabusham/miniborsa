package com.technzone.miniborsa.data.models.investor

import com.squareup.moshi.Json
import com.technzone.miniborsa.data.models.investor.Field
import java.io.Serializable

data class FieldsItem(

    @Json(name="field")
	val field: Field? = null,

    @Json(name="id")
	val id: Int? = null,

    @Json(name="value")
	val value: String? = null
):Serializable