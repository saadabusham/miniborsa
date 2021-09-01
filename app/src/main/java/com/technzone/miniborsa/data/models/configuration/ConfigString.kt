package com.technzone.miniborsa.data.models.configuration

import com.squareup.moshi.Json

data class ConfigString(

	@field:Json(name ="arabicTermsAndConditions")
	val arabicTermsAndConditions: String? = null,

	@field:Json(name ="englishTermsAndConditions")
	val englishTermsAndConditions: String? = null,

	@field:Json(name ="arabicNewVersionText")
	val arabicNewVersionText: String? = null,

	@field:Json(name ="englishNewVersionText")
	val englishNewVersionText: String? = null,

	@field:Json(name ="englishTellAFriend")
	val englishTellAFriend: String? = null,

	@field:Json(name ="arabicPrivacyPolicy")
	val arabicPrivacyPolicy: String? = null,

	@field:Json(name ="arabicTellAFriend")
	val arabicTellAFriend: String? = null,

	@field:Json(name ="medicationDeliveryNumber")
	val medicationDeliveryNumber: String? = null,

	@field:Json(name ="englishPrivacyPolicy")
	val englishPrivacyPolicy: String? = null
)