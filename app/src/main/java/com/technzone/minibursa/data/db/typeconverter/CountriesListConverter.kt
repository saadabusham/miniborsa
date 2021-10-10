package com.technzone.minibursa.data.db.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.technzone.minibursa.data.models.general.Countries
import java.lang.reflect.Type

class CountriesListConverter {

    @TypeConverter
    fun storedStringToObject(value: String?): List<Countries>? {
        val listType: Type = object : TypeToken<List<Countries>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun objectToStoredString(list: List<Countries>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}