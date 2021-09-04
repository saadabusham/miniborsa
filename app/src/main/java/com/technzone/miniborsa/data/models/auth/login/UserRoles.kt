package com.technzone.miniborsa.data.models.auth.login

import androidx.lifecycle.MutableLiveData
import com.squareup.moshi.Json

data class UserRoles(

	@field:Json(name ="name")
	val name: String? = null,

	@field:Json(name ="id")
	val id: Int? = null,

	@field:Json(name ="role")
	val role: Int? = null,

	@field:Json(name ="selected")
	val selected: MutableLiveData<Boolean> = MutableLiveData(false)
)