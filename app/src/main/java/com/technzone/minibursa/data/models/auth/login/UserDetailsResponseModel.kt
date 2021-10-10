package com.technzone.minibursa.data.models.auth.login

import com.google.gson.annotations.SerializedName

data class UserDetailsResponseModel(

	@field:SerializedName("lastName")
	val lastName: String? = null,

	@field:SerializedName("gender")
	val gender: Int? = null,

	@field:SerializedName("roles")
	val roles: MutableList<UserRoles>? = null,

	@field:SerializedName("fullName")
	val fullName: String? = null,

	@field:SerializedName("active")
	val active: Boolean? = null,

	@field:SerializedName("dateOfBirth")
	val dateOfBirth: String? = null,

	@field:SerializedName("picture")
	val picture: String? = null,

	@field:SerializedName("token")
	val token: String? = null,

	@field:SerializedName("firstName")
	val firstName: String? = null,

	@field:SerializedName("phoneNumber")
	val phoneNumber: String? = null,

	@field:SerializedName("createdDate")
	val createdDate: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("refreshToken")
	val refreshToken: RefreshToken? = null,

	@field:SerializedName("username")
	val username: String? = null
)