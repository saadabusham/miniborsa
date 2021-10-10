package com.technzone.minibursa.data.interceptors

import android.text.TextUtils
import com.technzone.minibursa.data.common.NetworkConstants.ACCEPT_LANGUAGE_HEADER_KEY
import com.technzone.minibursa.data.common.NetworkConstants.AUTHORIZATION_HEADER_KEY
import com.technzone.minibursa.data.common.NetworkConstants.AUTHORIZATION_HEADER_STARTED_VALUE
import com.technzone.minibursa.data.common.NetworkConstants.SKIP_AUTHORIZATION_HEADER
import com.technzone.minibursa.data.pref.configuration.ConfigurationPref
import com.technzone.minibursa.data.pref.user.UserPref
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*
import javax.inject.Inject

class AppBaseInterceptor @Inject constructor(
    private val userPref: UserPref,
    private val configurationPref: ConfigurationPref
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val newRequest = chain.request().newBuilder()

        if (TextUtils.isEmpty(original.header(SKIP_AUTHORIZATION_HEADER)) ||
            !TextUtils.isEmpty(original.header(SKIP_AUTHORIZATION_HEADER)) && original.header(
                SKIP_AUTHORIZATION_HEADER
            )!!.toLowerCase(Locale.ROOT) == "false"
        ) {

            newRequest.addHeader(
                AUTHORIZATION_HEADER_KEY,
                AUTHORIZATION_HEADER_STARTED_VALUE + getAccessToken()
            )
        }

        newRequest.addHeader(
            ACCEPT_LANGUAGE_HEADER_KEY,
            getAppLanguage()
        )
        return chain.proceed(newRequest.build())
    }

    private fun getAppLanguage(): String {
        return configurationPref.getAppLanguageValue()
    }

    private fun getAccessToken(): String {
        return userPref.getAccessToken()
    }
}