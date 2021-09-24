package com.technzone.miniborsa.data.db.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.technzone.miniborsa.data.models.Media
import com.technzone.miniborsa.data.models.investor.PropertiesItem
import java.lang.reflect.Type

class PropertiesListConverter {

    @TypeConverter
    fun storedStringToObject(value: String?): List<PropertiesItem>? {
        val listType: Type = object : TypeToken<List<PropertiesItem>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun objectToStoredString(list: List<PropertiesItem>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}