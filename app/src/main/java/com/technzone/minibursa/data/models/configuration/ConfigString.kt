package com.technzone.minibursa.data.models.configuration

import com.google.gson.annotations.SerializedName

data class ConfigString(

	@field:SerializedName("arabicTermsAndConditions")
	val arabicTermsAndConditions: String? = null,

	@field:SerializedName("englishTermsAndConditions")
	val englishTermsAndConditions: String? = null,

	@field:SerializedName("arabicNewVersionText")
	val arabicNewVersionText: String? = null,

	@field:SerializedName("englishNewVersionText")
	val englishNewVersionText: String? = null,

	@field:SerializedName("englishTellAFriend")
	val englishTellAFriend: String? = null,

	@field:SerializedName("arabicPrivacyPolicy")
	val arabicPrivacyPolicy: String? = null,

	@field:SerializedName("arabicTellAFriend")
	val arabicTellAFriend: String? = null,

	@field:SerializedName("englishPrivacyPolicy")
	val englishPrivacyPolicy: String? = null
)