package com.technzone.miniborsa.data.models.auth.login

import androidx.lifecycle.MutableLiveData
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserRoles(

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("name")
	val role: String? = null,

	val selected: MutableLiveData<Boolean> = MutableLiveData(false)
):Serializable