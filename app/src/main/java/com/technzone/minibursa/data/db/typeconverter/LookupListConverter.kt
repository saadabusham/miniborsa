package com.technzone.minibursa.data.db.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.technzone.minibursa.data.models.general.GeneralLookup
import java.lang.reflect.Type

class LookupListConverter {

    @TypeConverter
    fun storedStringToObject(value: String?): List<GeneralLookup>? {
        val listType: Type = object : TypeToken<List<GeneralLookup>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun objectToStoredString(list: List<GeneralLookup>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}