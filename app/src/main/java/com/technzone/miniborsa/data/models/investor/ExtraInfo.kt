package com.technzone.miniborsa.data.models.investor

import androidx.lifecycle.MutableLiveData
import com.squareup.moshi.Json
import java.io.Serializable

data class ExtraInfo(

	@Json(name="name")
	val name: String? = null,

	@Json(name="id")
	val id: Int? = null,
	var selected:MutableLiveData<Boolean> = MutableLiveData(false)
):Serializable