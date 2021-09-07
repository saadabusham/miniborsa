package com.technzone.miniborsa.data.models.auth.login

import com.squareup.moshi.Json

data class UserDetailsResponseModel(

    @field:Json(name = "lastName")
    val lastName: String? = null,

    @field:Json(name = "gender")
    val gender: Int? = null,

    @field:Json(name = "roles")
    val roles: List<UserRoles?>? = null,

    @field:Json(name = "fullName")
    val fullName: String? = null,

    @field:Json(name = "active")
    val active: Boolean? = null,

    @field:Json(name = "dateOfBirth")
    val dateOfBirth: String? = null,

    @field:Json(name = "token")
    val token: String? = null,

    @field:Json(name = "firstName")
    val firstName: String? = null,

    @field:Json(name = "phoneNumber")
    val phoneNumber: String? = null,

    @field:Json(name = "createdDate")
    val createdDate: String? = null,

    @field:Json(name = "id")
    val id: String? = null,

    @field:Json(name = "email")
    val email: String? = null,

    @field:Json(name = "refreshToken")
    val refreshToken: RefreshToken? = null,

    @field:Json(name = "username")
    val username: String? = null,

    @field:Json(name = "picture")
    val picture: String? = null
)