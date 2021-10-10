package com.technzone.minibursa.data.pref.user

import com.google.gson.Gson
import com.technzone.minibursa.data.enums.UserEnums
import com.technzone.minibursa.data.enums.UserRoleEnums
import com.technzone.minibursa.data.models.auth.login.UserDetailsResponseModel
import com.technzone.minibursa.utils.pref.PrefConstants.ACCESS_TOKEN_VALUE
import com.technzone.minibursa.utils.pref.PrefConstants.CURRENT_ROLE
import com.technzone.minibursa.utils.pref.PrefConstants.FIRST_LOGIN
import com.technzone.minibursa.utils.pref.PrefConstants.NOTIFICATION_SWITCH_VALUE
import com.technzone.minibursa.utils.pref.PrefConstants.TOUCH_ID_SWITCH_VALUE
import com.technzone.minibursa.utils.pref.PrefConstants.USER_PASSWORD_VALUE
import com.technzone.minibursa.utils.pref.PrefConstants.USER_STATUS_VALUE
import com.technzone.minibursa.utils.pref.SharedPreferencesUtil
import javax.inject.Inject

class UserPrefImp @Inject constructor(private val prefUtil: SharedPreferencesUtil) :
    UserPref {

    override fun setIsFirstOpen(isFirstOpen: Boolean) {
        prefUtil.setBooleanPreferences("first_open", isFirstOpen)
    }

    override fun getIsFirstOpen(): Boolean {
      return  prefUtil.getBooleanPreferences("first_open", true)
    }

    override fun setIsFirstLogin(isFirstLogin: Boolean) {
        prefUtil.setBooleanPreferences(FIRST_LOGIN, isFirstLogin)
    }

    override fun getIsFirstLogin(): Boolean {
        return prefUtil.getBooleanPreferences(FIRST_LOGIN, true)
    }

    override fun saveAccessToken(accessToken: String) {
        prefUtil.setStringPreferences(ACCESS_TOKEN_VALUE, accessToken)
    }

    override fun getAccessToken(): String {
        return prefUtil.getStringPreferences(ACCESS_TOKEN_VALUE, "")
    }

    override fun saveUserPassword(password: String) {
        prefUtil.setStringPreferences(USER_PASSWORD_VALUE, password)
    }

    override fun getUserPassword(): String {
        return prefUtil.getStringPreferences(USER_PASSWORD_VALUE, "")
    }

    override fun setNotificationStatus(flag: Boolean) {
        prefUtil.setBooleanPreferences(NOTIFICATION_SWITCH_VALUE, flag)
    }

    override fun getNotificationStatus(): Boolean {
        return prefUtil.getBooleanPreferences(NOTIFICATION_SWITCH_VALUE, true)
    }

    override fun setTouchIdStatus(flag: Boolean) {
        prefUtil.setBooleanPreferences(TOUCH_ID_SWITCH_VALUE, flag)
    }

    override fun getTouchIdStatus(): Boolean {
        return prefUtil.getBooleanPreferences(TOUCH_ID_SWITCH_VALUE, false)
    }

    override fun setUserStatus(ordinal: Int) {
        prefUtil.setIntPreferences(USER_STATUS_VALUE, ordinal)
    }

    override fun getUserStatus(): Int {
        return prefUtil.getIntPreferences(
            USER_STATUS_VALUE,
            UserEnums.UserState.NotLoggedIn.ordinal
        )
    }

    override fun getUser(): UserDetailsResponseModel? {
        val gson = Gson()
        val json: String = prefUtil.getStringPreferences("user", "") ?: " "
        return gson.fromJson(json, UserDetailsResponseModel::class.java)
    }


    override fun setUser(value: UserDetailsResponseModel?) {
        val gson = Gson()
        val json = gson.toJson(value)
        prefUtil.setStringPreferences("user", json)
    }

    override fun setCurrentRole(role: String) {
        prefUtil.setStringPreferences(CURRENT_ROLE, role)
    }

    override fun getCurrentRole(): String {
        return prefUtil.getStringPreferences(
            CURRENT_ROLE,
            UserRoleEnums.GUEST_ROLE.value
        )
    }


    override fun logout() {
        setUserStatus(UserEnums.UserState.NotLoggedIn.ordinal)
        saveAccessToken("")
    }
}