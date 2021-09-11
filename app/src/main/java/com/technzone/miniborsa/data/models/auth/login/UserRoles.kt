package com.technzone.miniborsa.data.models.auth.login

import androidx.lifecycle.MutableLiveData
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

data class UserRoles(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("role")
	val role: Int? = null,

	val selected: MutableLiveData<Boolean> = MutableLiveData(false)
)