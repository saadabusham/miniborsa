package com.technzone.minibursa.data.db.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.technzone.minibursa.data.models.investor.investors.CategoriesItem
import java.lang.reflect.Type

class CategoriesListConverter {

    @TypeConverter
    fun storedStringToObject(value: String?): List<CategoriesItem>? {
        val listType: Type = object : TypeToken<List<CategoriesItem>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun objectToStoredString(list: List<CategoriesItem>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}