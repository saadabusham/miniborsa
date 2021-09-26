package com.technzone.miniborsa.data.common

object Constants {

    const val NEWS_ID: String = "NEWS_ID"
    const val DEVICE_TYPE = 1
    const val APPLICATION_TYPE = 1

    const val PAGE_SIZE = 10

    //    Bundle Constants
    const val title: String = "TITLE"
    const val body: String = "BODY"

    object BundleData {

        const val COMPANY_DRAFT: String ="comapnyDraft"
        const val HAS_BUSINESS: String = "hasBusiness"
        const val OWNER_BUSINESS: String = "ownerBusiness"
        const val BUSINESS_ID: String = "businessId"
        const val SHOW_BACK: String = "showBack"
        const val INVESTOR_ID: String = "investorId"
        const val ADDRESS: String = "address"
        const val ACTION_TITLE: String = "actionTitle"
        const val TITLE: String = "title"
        const val COUNTRY: String = "country"
        const val BUSINESS_TYPE: String = "BusinessType"
        const val NEWS_ID: String = "newsId"
        const val IMAGE: String = "image"
        const val BUSINESS: String = "business"
        const val IS_LOGIN_SUCCESS: String = "isLoginSuccess"
        const val IS_ACTIVITY_RESULT: String = "isActivityResult"
        const val GENERAL_LIST: String = "generalList"
    }

    object NotificationsChannels {
        const val DEFAULT_CHANNEL: String = "Default channel"
    }

    object Twilio {
        const val CHAT_PUSH_CREDENTIALS: String = "CR39f07bd81e29ee23c8410d690b455eab"
        const val VOICE_PUSH_CREDENTIALS: String = "CR39f07bd81e29ee23c8410d690b455eab"
        const val CHAT_SID: String = "chat_sid"
        const val USER_ID: String = "user_id"
        const val USER_NAME: String = "user_name"
        const val USER_PICTURE: String = "user_picture"
        const val CHANNEL_ID: String = "channelId"
    }
}