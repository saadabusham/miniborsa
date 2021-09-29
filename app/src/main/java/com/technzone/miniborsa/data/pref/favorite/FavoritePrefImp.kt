package com.technzone.miniborsa.data.pref.favorite

import com.technzone.miniborsa.data.common.Constants
import com.technzone.miniborsa.utils.pref.SharedPreferencesUtil
import javax.inject.Inject

class FavoritePrefImp @Inject constructor(private val prefUtil: SharedPreferencesUtil) :
    FavoritePref {

    override fun setFavoriteList(list: List<Int>) {
        val gson = com.google.gson.Gson()
        val json = gson.toJson(list)
        prefUtil.setStringPreferences(Constants.BundleData.FAVORITE_LIST, json)
    }

    override fun addFavorite(id: Int) {
        val list = getFavoriteList().toMutableList()
        if (!list.contains(id)) {
            list.add(id)
            setFavoriteList(list)
        }
    }

    override fun removeFavorite(id: Int) {
        val list = getFavoriteList().toMutableList()
        list.remove(id)
        setFavoriteList(list)
    }

    override fun getFavoriteList(): List<Int> {
        val gson = com.google.gson.Gson()
        val json: String = prefUtil.getStringPreferences(Constants.BundleData.FAVORITE_LIST, "")
        if (json.isNullOrEmpty())
            return mutableListOf()
        return gson.fromJson(json, Array<Int>::class.java).asList()
    }
}