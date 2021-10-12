package com.technzone.minibursa.data.models.plan

import com.google.gson.annotations.SerializedName

data class CreatedBy(

	@field:SerializedName("lastName")
	val lastName: String? = null,

	@field:SerializedName("applicationType")
	val applicationType: Int? = null,

	@field:SerializedName("cards")
	val cards: List<CardsItem?>? = null,

	@field:SerializedName("gender")
	val gender: Int? = null,

	@field:SerializedName("isVerified")
	val isVerified: Boolean? = null,

	@field:SerializedName("lastLoginDate")
	val lastLoginDate: String? = null,

	@field:SerializedName("subscription")
	val subscription: List<SubscriptionItem?>? = null,

	@field:SerializedName("passwordHash")
	val passwordHash: String? = null,

	@field:SerializedName("provider")
	val provider: Int? = null,

	@field:SerializedName("registrationId")
	val registrationId: String? = null,

	@field:SerializedName("company")
	val company: Company? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("passwordSalt")
	val passwordSalt: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("channelId")
	val channelId: String? = null,

	@field:SerializedName("deviceType")
	val deviceType: Int? = null,

	@field:SerializedName("active")
	val active: Boolean? = null,

	@field:SerializedName("dateOfBirth")
	val dateOfBirth: String? = null,

	@field:SerializedName("picture")
	val picture: String? = null,

	@field:SerializedName("verificationCode")
	val verificationCode: String? = null,

	@field:SerializedName("firstName")
	val firstName: String? = null,

	@field:SerializedName("userRoles")
	val userRoles: List<UserRolesItem?>? = null,

	@field:SerializedName("companyId")
	val companyId: Int? = null,

	@field:SerializedName("phoneNumber")
	val phoneNumber: String? = null,

	@field:SerializedName("createdDate")
	val createdDate: String? = null,

	@field:SerializedName("allowNotifications")
	val allowNotifications: Boolean? = null,

	@field:SerializedName("refreshTokenValidUntil")
	val refreshTokenValidUntil: String? = null,

	@field:SerializedName("notifications")
	val notifications: List<NotificationsItem?>? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("refreshToken")
	val refreshToken: String? = null
)