package com.technzone.miniborsa.utils.pref

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.technzone.miniborsa.data.common.Constants
import com.technzone.miniborsa.data.enums.UserEnums
import com.technzone.miniborsa.data.models.configuration.ConfigurationWrapperResponse

class SharedPreferencesUtil private constructor() {

    private lateinit var oSharedPreferences: SharedPreferences
    private lateinit var oEditor: SharedPreferences.Editor

    companion object {

        @Volatile
        private var INSTANCE: SharedPreferencesUtil? = null

        fun getInstance(context: Context): SharedPreferencesUtil =
            INSTANCE
                ?: SharedPreferencesUtil(
                    context
                )
                    .also { INSTANCE = it }
    }

    private constructor(context: Context) : this() {
        oSharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
        oEditor = oSharedPreferences.edit()
        oEditor.apply()
    }

    fun clearPreference(): Boolean {
        return oEditor.clear().commit()
    }

    fun logout() {
        setIntPreferences(PrefConstants.USER_STATUS_VALUE, UserEnums.UserState.NotLoggedIn.ordinal)
        setStringPreferences(PrefConstants.ACCESS_TOKEN_VALUE, "")
    }

    // Long getter and setter
    fun getLongPreferences(key: String?, defaultValue: Long): Long {
        return oSharedPreferences.getLong(key, defaultValue)
    }

    fun setLongPreferences(key: String?, value: Long) {
        oEditor.putLong(key, value).apply()
    }

    // Int getter and setter
    fun getIntPreferences(key: String?, defaultValue: Int): Int {
        return oSharedPreferences.getInt(key, defaultValue)
    }

    fun setIntPreferences(key: String?, value: Int) {
        oEditor.putInt(key, value).apply()
    }

    // Float getter and setter
    fun getFloatPreferences(key: String?, defaultValue: Float): Float {
        return oSharedPreferences.getFloat(key, defaultValue)
    }

    fun setFloatPreferences(key: String?, value: Float) {
        oEditor.putFloat(key, value).apply()
    }

    // String getter and setter
    fun getStringPreferences(key: String?, defaultValue: String): String {
        return oSharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun setStringPreferences(key: String?, value: String) {
        oEditor.putString(key, value).apply()
    }

    // Boolean getter and setter
    fun getBooleanPreferences(key: String?, defaultValue: Boolean): Boolean {
        return oSharedPreferences.getBoolean(key, defaultValue)
    }

    fun setBooleanPreferences(key: String?, value: Boolean) {
        oEditor.putBoolean(key, value).apply()
    }

    // Double getter and setter
    fun getDoublePreferences(key: String?, defaultValue: Double): Double {
        return oSharedPreferences.getString(key, defaultValue.toString())?.toDouble()
            ?: defaultValue
    }

    fun setDoublePreferences(key: String?, value: Double) {
        oEditor.putString(key, value.toString()).apply()
    }

    // Strings Set getter and setter
    fun getStringSetPreferences(key: String?, defaultValue: Set<String>): MutableSet<String?>? {
        return oSharedPreferences.getStringSet(key, defaultValue)
    }

    fun setStringSetPreferences(key: String?, value: Set<String>) {
        oEditor.putStringSet(key, value).apply()
    }

    fun getConfigurationPreferences(): ConfigurationWrapperResponse {
        val gson = Gson()
        val json: String = oSharedPreferences.getString("config", "") ?: " "
        return gson.fromJson(json, ConfigurationWrapperResponse::class.java)
    }

    fun setConfigurationPreferences(value: ConfigurationWrapperResponse?) {
        val gson = Gson()
        val json = gson.toJson(value)
        oEditor.putString("config", json)
        oEditor.apply()
    }

    fun setIsNewNotifications(value: Boolean) {
        oEditor.putBoolean(Constants.NotificationsChannels.NEW_NOTIFICATIONS, value).apply()
    }

    fun getIsNewNotifications(): Boolean {
        return oSharedPreferences.getBoolean(Constants.NotificationsChannels.NEW_NOTIFICATIONS, false)
    }

}